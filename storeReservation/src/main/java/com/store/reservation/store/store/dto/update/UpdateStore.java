package com.store.reservation.store.store.dto.update;

import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class UpdateStore {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String storeName;
        private Location location;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String storeName;
        private double startRating;
        private List<Food> foods;
        private Location location;
        public static UpdateStore.Response fromEntity(Store store) {
            return Response.builder()
                    .storeName(store.getStoreName())
                    .startRating(store.getStarRating())
                    .foods(store.getFoods())
                    .location(store.getLocation())
                    .build();
        }
    }
}
