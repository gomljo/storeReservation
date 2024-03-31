package com.store.reservation.reservation.validator.arrivalState;

import com.store.reservation.reservation.constants.state.ArrivalState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ArrivalStateValidator implements ConstraintValidator<ArrivalStateCheck, String> {
    @Override
    public void initialize(ArrivalStateCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return ArrivalState.validate(value);
    }
}
