package affableBean.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
import affableBean.service.MemberService;
import affableBean.service.PasswordDto;
import affableBean.validation.EmailExistsException;
import affableBean.validation.ValidationResponse;

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

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    public RegistrationController() {
        super();
    }

	/**
	 * Auth process
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST } )
	public String loginConsole(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "message", required = false) String message, ModelMap mm) {
        LOGGER.info("In LoginConsole: " + error);

		if(error != null) {
			mm.put("message", "Login Failed!");
		} else if (message != null){
			mm.put("message", message);
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
		if (newMember != null) {
			memberDto = new MemberDto();
			memberDto.CopyFromModel(newMember);
		}
        LOGGER.info("in newMember, memberDto: " + memberDto);

		mm.put("memberDto", memberDto);
		
		return "registration/memberregistration";
		
	}
	
    private Member createMemberAccount(final MemberDto accountDto) { // , ModelMap mm) {
        Member registered = null;
        LOGGER.info("in createMemberAccount");
        try {
        	accountDto.setName(accountDto.getEmail());
            registered = userService.registerNewMemberAccount(accountDto);
        } catch (final EmailExistsException e) {
        	LOGGER.info("throwing EmailExistsException");
//        	mm.put("emailExists", true);
            return null;
        }
        return registered;
    }
    
    // Registration

//    @RequestMapping(value = "/user/registration", method = RequestMethod.POST, consumes="application/json")
    @RequestMapping(value = "/user/registration", method = RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
//    public ValidationResponse registerMemberAccount(@Valid @RequestBody MemberDto memberDto, 
    public ValidationResponse registerMemberAccount(@Valid @ModelAttribute MemberDto memberDto, 
    		Errors errors, final HttpServletRequest request)  {
        LOGGER.debug("Registering user account with information: {}", memberDto);
        ValidationResponse res = new ValidationResponse();
        HashMap<String, String> errorMessages = new HashMap<String, String>();
		if (errors.getErrorCount() > 0) {
			for (FieldError fieldError : errors.getFieldErrors()) {
                errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
				LOGGER.warn("Errors: " + fieldError.getField() + "  " + fieldError.getDefaultMessage());
			}
            res.setErrorMessageList(errorMessages);
            res.setStatus("failure");
			return res;
		}
        LOGGER.info("Registering user account with information: {}", memberDto);
        final Member registered = createMemberAccount(memberDto);	// , mm);
        if (registered == null) {
            errorMessages.put("email", "Email already exists");
            res.setErrorMessageList(errorMessages);
            res.setStatus("failure");
			return res;
        }
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        res.setStatus("success");
        return res;
    }

//	@RequestMapping(value = "/newMemberSubmit", method = RequestMethod.POST)
//	public String newMemberSubmit(@Valid final MemberDto memberDto, Errors errors,
////			final BindingResult bindingResult, 
////			@RequestParam("matchingPassword") String matchingPassword,
//			HttpServletRequest request,
//			ModelMap mm) {
//
//		LOGGER.info("Registering user account with information: {}", memberDto);
//		mm.put("memberDto", memberDto);
//
//		if (errors.hasErrors()) {
//			LOGGER.warn("Errors: ", errors.toString());
//			mm.put("passwordNoMatchError", true);
//			return "registration/memberregistration";
//		}
////		if (!memberDto.getPassword().equals(matchingPassword)) {
////			mm.put("passwordNoMatchError", true);
////			return "registration/memberregistration"; 
////		}
//		final Member registered = createMemberAccount(memberDto, mm);
//		if (registered == null) {
//			LOGGER.info("Registering user account , registered == null");
//			
//			return "registration/memberregistration";
//		}
//		try {
//			final String appUrl = "http://" + request.getServerName() + ":"
//					+ request.getServerPort() + request.getContextPath();
//			LOGGER.info("about to publish OnRegistrationCompleteEvent , "
//					+ appUrl);
//			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
//					registered, request.getLocale(), appUrl));
//		} catch (final Exception ex) {
//			LOGGER.warn("Unable to register user", ex);
//			 return "registration/emailError";
////			 return new ModelAndView("registration/emailError", "memberDto", memberDto);
////			return "front_store/memberregistration";
//		}
//		return "registration/successRegister";
//	}
	
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

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Locale locale, final Model model, @RequestParam("token") final String token) {
		LOGGER.info("confirmRegistration");
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
        LOGGER.info("in resetPassword: " + userEmail);
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
		// return "registration/successRegister";
    }

    
    @RequestMapping(value = "/registration/forgotPassword", method = RequestMethod.GET)
    public String showForgotPasswordPage(HttpSession session, final HttpServletRequest request, HttpServletResponse response, final Model model) {
        LOGGER.info("in showForgotPasswordPage");
//        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
//		// Spring Security will allow the Token to be included in this header
//		// name
//		response.setHeader("X-CSRF-HEADER", token.getHeaderName());
//		// Spring Security will allow the token to be included in this parameter
//		// name
//		response.setHeader("X-CSRF-PARAM", token.getParameterName());
//		// this is the value of the token to be included as either a header or
//		// an HTTP parameter
//		response.setHeader("X-CSRF-TOKEN", token.getToken());
//        String token = CsrfTokenManager.getTokenForSession(session);
//        model.addAttribute("csrfToken", token);
//        LOGGER.info("csrfToken:" + token);
        return "/registration/forgotPassword";
    }
    
    @RequestMapping(value = "/user/getNewPasswordFromExisting", method = RequestMethod.GET)
    public String getNewPassworFromExistingPage(final ModelMap mm) {
        LOGGER.info("in getNewPasswordFromExistingPage");
        PasswordDto passwordDto = new PasswordDto();
		mm.put("passwordDto", passwordDto);
//        model.addAttribute("passConfirm", "");
//        return "/registration/updatePassword";
        return "/registration/getNewPasswordFromExisting";
    }
    
    @RequestMapping(value = "/user/getNewPasswordFromScratch", method = RequestMethod.GET)
    public String getNewPasswordFromScratchPage(final ModelMap mm) {
        LOGGER.info("in getNewPasswordFromExistingPage");
        PasswordDto passwordDto = new PasswordDto();
		mm.put("passwordDto", passwordDto);
//        model.addAttribute("passConfirm", "");
//        return "/registration/updatePassword";
        return "/registration/getNewPasswordFromScratch";
    }
    
    @RequestMapping(value = "/user/getNewPasswordFromEmail", method = RequestMethod.GET)
    public String getNewPasswordFromEmail(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        LOGGER.info("in getNewPasswordFromEmail, token: " + token);
        final PasswordResetToken passToken = userService.getPasswordResetToken(token);
        final Member user = passToken.getMember();
        LOGGER.info("in getNewPasswordFromScratch, user: " + user);
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

        final Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        LOGGER.info("about to redirect:/getNewPasswordFromScratch.html");

        return "redirect:/user/getNewPasswordFromScratch.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/user/savePasswordFromScratch", method = RequestMethod.POST)
//  @PreAuthorize("hasRole('READ_PRIVILEGE')")
//  @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseBody
    public ValidationResponse savePasswordFromScratch(final Locale locale, @Valid @ModelAttribute final PasswordDto passwordDto, 
    		Errors errors) {
        LOGGER.info("in savePasswordFromScratch");
        ValidationResponse res = new ValidationResponse();
        HashMap<String, String> errorMessages = new HashMap<String, String>();
		if (errors.getErrorCount() > 0) {
			for (FieldError fieldError : errors.getFieldErrors()) {
                errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
				LOGGER.warn("Errors: " + fieldError.getField() + "  " + fieldError.getDefaultMessage());
			}
            res.setErrorMessageList(errorMessages);
            res.setStatus("failure");
			return res;
		}
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        LOGGER.info("in savePassword, username: " + userName);
        Member member = userService.getMemberByEmail(userName);
        LOGGER.info("in savePassword, member: " + member);
        userService.changeMemberPassword(member, passwordDto.getPassword());
        res.setStatus("success");
        return res;
    }

    // change user password

    @RequestMapping(value = "/user/savePasswordFromExisting", method = RequestMethod.POST)
//    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public ValidationResponse savePasswordFromExisting(final Locale locale, @Valid @ModelAttribute final PasswordDto passwordDto, 
    		Errors errors, final HttpServletRequest request)  {
        LOGGER.info("in savePasswordFromExisting");
        ValidationResponse res = new ValidationResponse();
        HashMap<String, String> errorMessages = new HashMap<String, String>();
		if (errors.getErrorCount() > 0) {
			for (FieldError fieldError : errors.getFieldErrors()) {
                errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
				LOGGER.warn("Errors: " + fieldError.getField() + "  " + fieldError.getDefaultMessage());
			}
            res.setErrorMessageList(errorMessages);
            res.setStatus("failure");
			return res;
		}

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        LOGGER.info("in savePasswordFromExisting, username: " + userName);
        final Member user = userService.getMemberByEmail(userName);
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            errorMessages.put("oldPassword", "Invalid Old Password");
			LOGGER.warn("Errors: " + "oldPassword," + " Invalid Old Password");
            res.setErrorMessageList(errorMessages);
            res.setStatus("failure");
			return res;
        }
        userService.changeMemberPassword(user, passwordDto.getPassword());
        res.setStatus("success");
        return res;
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
        final String url = contextPath + "/user/getNewPasswordFromEmail?id=" + user.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
