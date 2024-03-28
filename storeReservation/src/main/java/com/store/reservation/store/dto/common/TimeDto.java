package com.store.reservation.store.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.reservation.store.domain.vo.operating.OperatingHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime openingHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime closingHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime breakTimeStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime breakTimeEnd;
    private int reservationTimeInterval;

    public static TimeDto from(OperatingHours operatingHours) {
        return TimeDto.builder()
                .openingHour(operatingHours.getOpeningHours())
                .closingHour(operatingHours.getClosingHours())
                .breakTimeStart(operatingHours.getStartTime())
                .breakTimeEnd(operatingHours.getEndTime())
                .build();
    }
}
