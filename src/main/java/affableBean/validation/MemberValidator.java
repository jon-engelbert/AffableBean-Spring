package affableBean.validation;


import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import affableBean.service.MemberDto;

public class MemberValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
        return MemberDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object obj, final Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "message.firstName", "Firstname is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "message.lastName", "LastName is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "message.password", "LastName is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "membername", "message.membername", "MemberName is required.");
    }

}