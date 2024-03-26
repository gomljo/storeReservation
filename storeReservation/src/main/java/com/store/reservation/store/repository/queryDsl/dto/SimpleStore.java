package com.store.reservation.store.repository.queryDsl.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.reservation.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SimpleStore {
    private String storeName;
    private String city;
    private String county;
    private String district;
    private String roadName;
    private double starRating;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime openingHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime closingHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime breakTimeStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime breakTimeEnd;
    private Long numberOfReviews;

    public static SimpleStore from(Store store) {
        return SimpleStore.builder()
                .storeName(store.getStoreName())
                .city(store.getLocation().getCity())
                .county(store.getLocation().getCounty())
                .district(store.getLocation().getDistrict())
                .roadName(store.getLocation().getRoadName())
                .starRating(store.getStarRating())
                .openingHour(store.getOperatingHours().getOpeningHours())
                .closingHour(store.getOperatingHours().getClosingHours())
                .breakTimeStart(store.getOperatingHours().getStartTime())
                .breakTimeEnd(store.getOperatingHours().getEndTime())
                .numberOfReviews(store.getNumberOfReviews())
                .build();
    }
}
