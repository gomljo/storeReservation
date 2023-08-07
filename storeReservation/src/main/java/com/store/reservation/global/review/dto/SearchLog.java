package com.store.reservation.global.review.dto;

import com.store.reservation.reservation.log.domain.ReservationLog;
import com.store.reservation.reservation.log.dto.ReservationLogDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

public class SearchLog {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private List<ReservationLogDto> reservationLogList;

        public static SearchLog.Response fromEntity(List<ReservationLog> reservationLogList){
            return new SearchLog.Response(
                    reservationLogList.stream()
                    .map(ReservationLogDto::fromEntity)
                    .collect(Collectors.toList()));
        }
    }
}
