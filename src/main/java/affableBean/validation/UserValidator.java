package affableBean.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import affableBean.service.MemberDto;

public class UserValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return MemberDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        System.out.println("in UserValidator#validate: ");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name", "Name is required.");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "message.password", "Password is required.");
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "message.email", "Email is required.");
    }

}
