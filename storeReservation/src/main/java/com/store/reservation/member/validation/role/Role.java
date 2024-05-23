package com.store.reservation.member.validation.role;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE_USE})
@Constraint(validatedBy = RoleValidator.class)
public @interface Role {
    String message() default "권한이 올바르지 않습니다.";

    String value() default "ROLE_VISITOR";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
