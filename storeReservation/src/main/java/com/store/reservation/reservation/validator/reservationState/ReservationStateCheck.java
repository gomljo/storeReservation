package com.store.reservation.reservation.validator.reservationState;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReservationStateValidator.class)
public @interface ReservationStateCheck {
    String message() default "예약 상태 값이 올바르지 않습니다.";

}
