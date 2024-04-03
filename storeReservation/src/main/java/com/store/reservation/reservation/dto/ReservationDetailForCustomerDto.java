package com.store.reservation.reservation.dto;

import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.dto.common.ReservationStateDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import com.store.reservation.store.domain.vo.location.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationDetailForCustomerDto {
    private ReservationStateDto reservationStateDto;
    private ReservationTimeDto reservationTimeDto;
    private Location location;
    private String storeName;

    public static ReservationDetailForCustomerDto from(Reservation reservation) {
        return ReservationDetailForCustomerDto.builder()
                .reservationTimeDto(ReservationTimeDto.builder()
                        .reservationDate(reservation.getTime().getReservedDate())
                        .reservationTime(reservation.getTime().getReservedTime())
                        .build())
                .reservationStateDto(ReservationStateDto.builder()
                        .reservationState(reservation.getState().getReservationState())
                        .arrivalState(reservation.getState().getArrivalState())
                        .build())
                .location(reservation.getStore().getLocation())
                .storeName(reservation.getStore().getStoreName())
                .build();
    }

}
