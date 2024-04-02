package com.store.reservation.reservation.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Time {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reservedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime reservedTime;

    public LocalDateTime getTime() {
        return LocalDateTime.of(this.reservedDate, this.reservedTime);
    }

}
