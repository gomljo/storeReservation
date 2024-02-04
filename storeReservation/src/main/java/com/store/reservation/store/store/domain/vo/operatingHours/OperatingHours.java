package com.store.reservation.store.store.domain.vo.operatingHours;

import com.store.reservation.reservation.reservationTimePolicy.domain.vo.BreakTime;
import com.store.reservation.reservation.reservationTimePolicy.domain.vo.BusinessHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OperatingHours {

    @Embedded
    private BreakTime breakTime;

    @Embedded
    private BusinessHours businessHours;
}
