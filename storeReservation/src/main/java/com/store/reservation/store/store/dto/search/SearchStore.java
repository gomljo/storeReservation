package com.store.reservation.store.store.dto.search;

import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchStore {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String storeName;
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


        public static SearchStore.Response fromEntity(Store store) {
            return SearchStore.Response.builder()
                    .storeName(store.getStoreName())
                    .startRating(store.getStarRating())
                    .foods(store.getFoods())
                    .location(store.getLocation())
                    .build();
        }

    }


}
