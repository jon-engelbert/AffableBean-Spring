package affableBean.controller;

import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import affableBean.domain.Member;
import affableBean.domain.PasswordResetToken;
import affableBean.domain.VerificationToken;
import affableBean.error.GenericResponse;
import affableBean.error.InvalidOldPasswordException;
import affableBean.error.UserAlreadyExistException;
import affableBean.error.UserNotFoundException;
import affableBean.registration.OnRegistrationCompleteEvent;
import affableBean.repository.MemberRepository;
import affableBean.service.IMemberService;
import affableBean.service.MemberDto;
import affableBean.validation.EmailExistsException;

@Controller
public class RegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
    private IMemberService userService;

    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

//    @Autowired
//    private MemberDetailsService userDetailsService;

    @Autowired
    private Environment env;

    public RegistrationController() {
        super();
    }

	/**
	 * Auth process
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST } )
	public String loginConsole(@RequestParam(value = "error", required = false) String error, ModelMap mm) {
        LOGGER.info("In LoginConsole: " + error);

		if(error != null) {
			mm.put("message", "Login Failed!");
		} else {
			mm.put("message", false);
		}
		return "admin/login";
	}

	@RequestMapping(value= "/newMember", method = RequestMethod.GET) 
	public String newMember(HttpSession session, HttpServletRequest request, ModelMap mm) {

        LOGGER.info("in newMember: " + userService.getCustomerFromRequest(request));
		Member newMember = userService.getCustomerFromRequest(request);
		MemberDto memberDto = new MemberDto();
		if (newMember != null)
			memberDto = new MemberDto(newMember);
        LOGGER.info("in newMember, memberDto: " + memberDto);

		mm.put("memberDto", memberDto);
		
		return "registration/memberregistration";
		
	}
	
    private Member createMemberAccount(final MemberDto accountDto, ModelMap mm) {
        Member registered = null;
        LOGGER.info("in createMemberAccount");
        try {
        	accountDto.setUsername(accountDto.getEmail());
            registered = userService.registerNewMemberAccount(accountDto);
        } catch (final EmailExistsException e) {
        	LOGGER.info("throwing EmailExistsException");
        	mm.put("emailExists", true);
            return null;
        }
        return registered;
    }
    
	@RequestMapping(value = "/newMemberSubmit", method = RequestMethod.POST)
	public String newMemberSubmit(@Valid final MemberDto memberDto,
			final BindingResult bindingResult, 
			@RequestParam("matchingPassword") String matchingPassword,
			HttpServletRequest request,
			ModelMap mm) {

		LOGGER.info("Registering user account with information: {}", memberDto);
		mm.put("memberDto", memberDto);

		if (!memberDto.getPassword().equals(matchingPassword)) {
			mm.put("passwordNoMatchError", true);
			return "registration/memberregistration"; 
		}
		final Member registered = createMemberAccount(memberDto, mm);
		if (registered == null) {
			LOGGER.info("Registering user account , registered == null");
			
			return "registration/memberregistration";
		}
//		try {
			final String appUrl = "http://" + request.getServerName() + ":"
					+ request.getServerPort() + request.getContextPath();
			LOGGER.info("about to publish OnRegistrationCompleteEvent , "
					+ appUrl);
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
					registered, request.getLocale(), appUrl));
//		} catch (final Exception ex) {
//			LOGGER.warn("Unable to register user", ex);
//			// return new ModelAndView("emailError", "memberDto", memberDto);
//			return "front_store/memberregistration";
//		}
		// return new ModelAndView("successRegister", "memberDto", memberDto);
		return "registration/successRegister";
	}
	
	// this comes from ajax call in memberregistration.html
	@RequestMapping("/checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam("email") String email) {
		LOGGER.debug("checking if email already exists");
		if (userService.checkEmailExists(email)) {
			return "exists";
		}
		return "unregistered";
	}

    // Registration

//    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
//    @ResponseBody
//    public GenericResponse registerMemberAccount(@Valid final MemberDto accountDto, final HttpServletRequest request, ModelMap mm) {
//        LOGGER.debug("Registering user account with information: {}", accountDto);
//
//        final Member registered = createMemberAccount(accountDto, mm);
//        if (registered == null) {
//            throw new UserAlreadyExistException();
//        }
//        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
//
//        return new GenericResponse("success");
//    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Locale locale, final Model model, @RequestParam("token") final String token) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/registration/badUser.html?lang=" + locale.getLanguage();
        }

        final Member user = verificationToken.getMember();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return "redirect:/registration/badUser.html?lang=" + locale.getLanguage();
        }
        LOGGER.info("confirmRegistration: " + verificationToken);
        user.setEnabled(true);
        userService.saveRegisteredCustomer(user);
        LOGGER.info("saved new customer enabled: " + user);
        model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
        LOGGER.info("added attribute: " + messages.getMessage("message.accountVerified", null, locale));
        return "redirect:/login?lang=" + locale.getLanguage();
    }

    // user activation - verification

    @RequestMapping(value = "/user/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final Member user = userService.getMember(newToken.getToken());
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        final SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, user);
        mailSender.send(email);

        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    // Reset password

    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail) {
        LOGGER.info("in resetPassword" + userEmail);
        final Member user = userService.getMemberByEmail(userEmail);
        if (user == null) {
            throw new UserNotFoundException();
        }

        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForMember(user, token);
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        final SimpleMailMessage email = constructResetTokenEmail(appUrl, request.getLocale(), token, user);
        mailSender.send(email);
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(final Model model) {
        LOGGER.info("in showChangePasswordPage");
        model.addAttribute("pass", "");
        model.addAttribute("passConfirm", "");
        return "/registration/updatePassword";
    }
    
    @RequestMapping(value = "/user/changePassword", method = RequestMethod.GET)
    public String changePassword(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        LOGGER.info("in changePassword, token: " + token);
        final PasswordResetToken passToken = userService.getPasswordResetToken(token);
        final Member user = passToken.getMember();
        if (passToken == null || user.getId() != id) {
            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            return "redirect:/login.html?lang=" + locale.getLanguage();
        }

//        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, userDetailsService.loadMemberByMembername(user.getEmail()).getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.info("about to redirect:/updatePassword.html");

        return "redirect:/registration/updatePassword.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/user/savePassword", method = RequestMethod.POST)
//    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public GenericResponse savePassword(final Locale locale, @RequestParam("password") final String password) {
        LOGGER.info("in savePassword");
        final Member user = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.changeMemberPassword(user, password);
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    // change user password

    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public GenericResponse changeMemberPassword(final Locale locale, @RequestParam("password") final String password, @RequestParam("oldpassword") final String oldPassword) {
        final Member user = userService.getMemberByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!userService.checkIfValidOldPassword(user, oldPassword)) {
            throw new InvalidOldPasswordException();
        }
        userService.changeMemberPassword(user, password);
        return new GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

    // NON-API

    private final SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final Member user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Resend Registration Token");
        email.setText(message + " \r\n" + confirmationUrl);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private final SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final Member user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
