package com.store.reservation.reservation.reservationTimePolicy.domain.model;

import com.store.reservation.reservation.reservationTimePolicy.domain.vo.BreakTime;
import com.store.reservation.reservation.reservationTimePolicy.domain.vo.BusinessHours;
import com.store.reservation.reservation.reservationTimePolicy.dto.ReservationTimePolicyStateDto;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReservationTimePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @DateTimeFormat(pattern = "kk:mm:ss")
    private List<LocalTime> predefinedTime;

    @Embedded
    private BusinessHours businessHours;

    @Embedded
    private BreakTime breakTime;

    @OneToOne
    @JoinColumn(name = "store_Id")
    private Store store;

    public void update(ReservationTimePolicyStateDto reservationTimePolicyStateDto) {

    }

}
