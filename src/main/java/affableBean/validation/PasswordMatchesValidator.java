package affableBean.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import affableBean.service.MemberDto;
import affableBean.service.PasswordDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        MemberDto user = null;
        PasswordDto pwDto = null;
        String password = "", match = "";
        if (obj instanceof MemberDto) {
        	user = (MemberDto) obj;
        	password = user.getPassword();
        	match = user.getMatchingPassword();
        	System.out.println("MemberDto Password: " + password + ", Match: " + match);
        } else if (obj instanceof PasswordDto) {
        	pwDto = (PasswordDto) obj;
        	password = pwDto.getPassword();
        	match = pwDto.getMatchingPassword();
        	System.out.print("PasswordDTO password: " + password + ", Match: " + match);
        }
    	return password.equals(match) && !password.isEmpty();
    }
}
