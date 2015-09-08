package affableBean.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import affableBean.service.MemberDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final MemberDto user = (MemberDto) obj;
        System.out.println("in PasswordMatchesValidator: " + user.getPassword() + ", " + user.getMatchingPassword() + ", " + user.getPassword().equals(user.getMatchingPassword()));
        return user.getPassword().equals(user.getMatchingPassword());
    }

}
