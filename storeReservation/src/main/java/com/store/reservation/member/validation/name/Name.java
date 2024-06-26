package com.store.reservation.member.validation.name;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NameValidator.class)
public @interface Name {

    String message() default "이름은 숫자를 포함하지 않습니다.";
    String value() default "개발자";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
