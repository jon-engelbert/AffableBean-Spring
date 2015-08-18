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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import affableBean.domain.Member;
import affableBean.domain.PasswordResetToken;
import affableBean.domain.VerificationToken;
import affableBean.registration.OnRegistrationCompleteEvent;
import affableBean.service.IMemberService;
import affableBean.service.MemberDto;
import affableBean.validation.EmailExistsException;
import affableBean.web.GenericResponse;
import affableBean.web.InvalidOldPasswordException;
import affableBean.web.UserNotFoundException;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IMemberService memberService;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;


//    @Autowired
//    private UserDetailsService userDetailsService;

    @Autowired
    private Environment env;

    public RegistrationController() {
        super();
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String signupConsole(HttpSession session, WebRequest request, ModelMap mm) {
	    LOGGER.debug("Rendering registration page.");
		MemberDto memberDto = new MemberDto();
	    mm.put("member", memberDto);
	    return "front_store/signup";
	}

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) {
        final Locale locale = request.getLocale();

        final VerificationToken verificationToken = memberService.getVerificationToken(token);
        if (verificationToken == null) {
            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badMember.html?lang=" + locale.getLanguage();
        }

        final Member member = verificationToken.getMember();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
            model.addAttribute("expired", true);
            model.addAttribute("token", token);
            return "redirect:/badMember.html?lang=" + locale.getLanguage();
        }

        member.setEnabled(true);
        memberService.saveRegisteredMember(member);
        model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
        return "redirect:/login.html?lang=" + locale.getLanguage();
    }

    @RequestMapping(value = "/member/registration", method = RequestMethod.POST)
    public ModelAndView registerMemberAccount(@ModelAttribute("member") @Valid final MemberDto memberDto, final HttpServletRequest request, final Errors errors) {
        LOGGER.debug("Registering member account with information: {}", memberDto);

        final Member registered = createMemberAccount(memberDto);
        if (registered == null) {
        	LOGGER.info("in registerMemberAccount, about to go to registration");
            // result.rejectValue("email", "message.regError");
            return new ModelAndView("registration", "member", memberDto);
        }
        try {
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(), appUrl));
        } catch (final Exception ex) {
            LOGGER.warn("Unable to register member", ex);
            return new ModelAndView("emailError", "member", memberDto);
        }
    	LOGGER.info("in registerMemberAccount, about to go to successRegister");
        return new ModelAndView("front_store/successRegister", "member", memberDto);
    }


    // member activation - verification

    @RequestMapping(value = "/member/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = memberService.generateNewVerificationToken(existingToken);
        final Member member = memberService.getMember(newToken.getToken());
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        final SimpleMailMessage email = constructResendVerificationTokenEmail(appUrl, request.getLocale(), newToken, member);
        mailSender.send(email);

        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

    // Reset password

    @RequestMapping(value = "/member/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse resetPassword(final HttpServletRequest request, @RequestParam("email") final String memberEmail) {
        final Member member = memberService.findMemberByEmail(memberEmail);
        if (member == null) {
            throw new UserNotFoundException();
        }

        final String token = UUID.randomUUID().toString();
        memberService.createPasswordResetTokenForMember(member, token);
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        final SimpleMailMessage email = constructResetTokenEmail(appUrl, request.getLocale(), token, member);
        mailSender.send(email);
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

//    @RequestMapping(value = "/member/changePassword", method = RequestMethod.GET)
//    public String showChangePasswordPage(final Locale locale, final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
//        final PasswordResetToken passToken = memberService.getPasswordResetToken(token);
//        final Member member = passToken.getMember();
//        if (passToken == null || member.getId() != id) {
//            final String message = messages.getMessage("auth.message.invalidToken", null, locale);
//            model.addAttribute("message", message);
//            return "redirect:/login.html?lang=" + locale.getLanguage();
//        }
//
//        final Calendar cal = Calendar.getInstance();
//        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
//            model.addAttribute("message", messages.getMessage("auth.message.expired", null, locale));
//            return "redirect:/login.html?lang=" + locale.getLanguage();
//        }
//
//        final Authentication auth = new UsernamePasswordAuthenticationToken(member, null, userDetailsService.loadUserByUsername(member.getEmail()).getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
//    }

    @RequestMapping(value = "/member/savePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public GenericResponse savePassword(final Locale locale, @RequestParam("password") final String password) {
        final Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberService.changeMemberPassword(member, password);
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
    }

    // change member password

    @RequestMapping(value = "/member/updatePassword", method = RequestMethod.POST)
    @PreAuthorize("hasRole('READ_PRIVILEGE')")
    @ResponseBody
    public GenericResponse changeMemberPassword(final Locale locale, @RequestParam("password") final String password, @RequestParam("oldpassword") final String oldPassword) {
        final Member member = memberService.findMemberByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!memberService.checkIfValidOldPassword(member, oldPassword)) {
            throw new InvalidOldPasswordException();
        }
        memberService.changeMemberPassword(member, password);
        return new affableBean.web.GenericResponse(messages.getMessage("message.updatePasswordSuc", null, locale));
    }

    // NON-API

    private final SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final Member member) {
        final String confirmationUrl = contextPath + "/regitrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Resend Registration Token");
        email.setText(message + " \r\n" + confirmationUrl);
        email.setTo(member.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private final SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final Member member) {
        final String url = contextPath + "/member/changePassword?id=" + member.getId() + "&token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(member.getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


    private Member createMemberAccount(final MemberDto accountDto) {
        Member registered = null;
        try {
            registered = memberService.registerNewMemberAccount(accountDto);
        } catch (final EmailExistsException e) {
            return null;
        }
        return registered;
    }
}
