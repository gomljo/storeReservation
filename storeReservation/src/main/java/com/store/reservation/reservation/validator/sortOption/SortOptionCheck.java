package com.store.reservation.reservation.validator.sortOption;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SortOptionValidator.class)
public @interface SortOptionCheck {
    String message() default "정렬 조건이 올바르지 않습니다.";

}
