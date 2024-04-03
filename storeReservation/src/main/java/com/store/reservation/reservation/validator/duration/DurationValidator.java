package com.store.reservation.reservation.validator.duration;

import com.store.reservation.reservation.constants.search.Duration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DurationValidator implements ConstraintValidator<DurationCheck, String> {
    @Override
    public void initialize(DurationCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return Duration.validate(value);
    }
}
