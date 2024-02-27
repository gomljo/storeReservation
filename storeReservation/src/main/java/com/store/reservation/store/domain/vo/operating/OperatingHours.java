package com.store.reservation.store.domain.vo.operating;

import com.store.reservation.store.dto.create.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    @Builder.Default
    private List<LocalTime> reservationTimes = new ArrayList<>();
    private Integer reservationTimeInterval;

    public void defineReservationTimes() {

        this.reservationTimes = new ArrayList<>();
        LocalTime current = businessHours.getOpeningHours();
        while (businessHours.isBeforeThenClosing(current)) {
            if (breakTime.isNotBreakTime(current)) {
                reservationTimes.add(current);
            }
            current = current.plusMinutes(this.reservationTimeInterval);
        }

    }
    public List<LocalTime> get(){
        return this.reservationTimes;
    }
    public static OperatingHours createBy(StoreDto storeDto) {

        return OperatingHours.builder()
                .businessHours(BusinessHours
                        .builder()
                        .openingHours(storeDto.getOpeningHour())
                        .closingHours(storeDto.getClosingHour())
                        .build())
                .breakTime(BreakTime.builder()
                        .startTime(storeDto.getBreakTimeStart())
                        .endTime(storeDto.getBreakTimeEnd())
                        .build())
                .reservationTimeInterval(storeDto.getReservationTimeInterval())
                .build();
    }
}
