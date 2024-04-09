package com.store.reservation.reservation.dto;


import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.reservation.constants.search.SortOption;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.validator.duration.DurationCheck;
import com.store.reservation.reservation.validator.reservationState.ReservationStateCheck;
import com.store.reservation.reservation.validator.sortOption.SortOptionCheck;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationSearchDto {
    @DurationCheck
    private Duration duration;
    @ReservationStateCheck
    private ReservationState reservationState;
    @SortOptionCheck
    private SortOption sortOption;
    private Pageable pageable;
}
