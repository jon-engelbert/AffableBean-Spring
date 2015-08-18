//package affableBean.validation;
//
//import org.springframework.validation.Errors;
//import org.springframework.validation.ValidationUtils;
//import org.springframework.validation.Validator;
//
//import affableBean.service.MemberDto;
//
//
//public class PasswordValidator implements Validator {
//
//	public boolean supports(Class<?> paramClass) {
//		return MemberDto.class.equals(paramClass);
//	}
//
//	public void validate(Object obj, Errors errors) {
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "valid.password");
//		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConf", "valid.passwordConf");
//		MemberDto member = (MemberDto) obj;
//		if (!member.getPassword().equals(member.getMatchingPassword())) {
//			errors.rejectValue("passwordConf", "valid.passwordConfDiff");
//		}
//	}
////    @Override
////    public boolean isValid(final String password, final ConstraintValidatorContext context) {
////        final PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 30), new UppercaseCharacterRule(1), new DigitCharacterRule(1), new SpecialCharacterRule(1), new WhitespaceRule()));
////        final RuleResult result = validator.validate(new PasswordData(password));
////        if (result.isValid()) {
////            return true;
////        }
////        context.disableDefaultConstraintViolation();
////        context.buildConstraintViolationWithTemplate(Joiner.on("\n").join(validator.getMessages(result))).addConstraintViolation();
////        return false;
////    }
//
//}