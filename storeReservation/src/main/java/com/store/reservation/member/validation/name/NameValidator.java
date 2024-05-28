package com.store.reservation.member.validation.name;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<Name, String> {
    private static final String NAME_PATTERN = "^[A-Za-zㄱ-ㅎ가-힣]*$";

    @Override
    public void initialize(Name constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return Pattern.matches(NAME_PATTERN, name);
    }
}
