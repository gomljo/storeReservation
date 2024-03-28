package com.store.reservation.store.dto.search.response;

import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.domain.vo.food.Food;
import com.store.reservation.store.domain.vo.location.Location;
import com.store.reservation.store.dto.common.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StoreDetailForManagerDto {

    private Long storeId;
    private String storeName;
    private Long numberOfReviews;
    private Double starRating;
    private Set<Food> foods;
    private TimeDto operationHours;
    private Location location;
    private List<LocalTime> reservationList;
    private Long numberOfReservationPerTime;

    public static StoreDetailForManagerDto from(Store store) {
        return StoreDetailForManagerDto.builder()
                .storeId(store.getId())
                .storeName(store.getStoreName())
                .numberOfReviews(store.getNumberOfReviews())
                .starRating(store.getStarRating())
                .foods(store.getFoods())
                .operationHours(TimeDto.from(store.getOperatingHours()))
                .location(store.getLocation())
                .reservationList(store.getOperatingHours().getReservationTimes())
                .numberOfReservationPerTime(store.getNumberOfReservationPerTime())
                .build();
    }
}
