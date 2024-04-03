package com.store.reservation.reservation.validator.sortOption;

import com.store.reservation.reservation.constants.search.SortOption;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SortOptionValidator implements ConstraintValidator<SortOptionCheck, String> {
    @Override
    public void initialize(SortOptionCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return SortOption.validate(value);
    }
}
