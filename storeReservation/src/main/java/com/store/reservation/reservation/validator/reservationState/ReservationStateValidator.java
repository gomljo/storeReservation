package com.store.reservation.reservation.validator.reservationState;

import com.store.reservation.reservation.constants.state.ReservationState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReservationStateValidator implements ConstraintValidator<ReservationStateCheck, String> {
    @Override
    public void initialize(ReservationStateCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return ReservationState.validate(value);
    }
}
