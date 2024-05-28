package com.store.reservation.member.validation.phoneNumber;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    String message() default "적합하지 않은 전화번호입니다.";
    String value() default "010-xxxx-xxxx";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
