package com.store.reservation.reservation.reservation.validator;

import com.store.reservation.store.store.dto.update.UpdateStore;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ReservationStateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateStore.Request.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
