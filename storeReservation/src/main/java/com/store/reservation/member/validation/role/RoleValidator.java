package com.store.reservation.member.validation.role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<Role, String> {
    @Override
    public void initialize(Role constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(com.store.reservation.member.domain.type.Role.values())
                .anyMatch(role -> role.name().equals(value));
    }
}
