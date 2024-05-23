package com.store.reservation.member.validation.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "비밀번호는 6~10자리이며 영문, 특수문자(~,!,$,*), 숫자를 포함합니다.";
    String value() default "1234!Qww";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
