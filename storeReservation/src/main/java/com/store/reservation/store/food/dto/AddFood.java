package com.store.reservation.store.food.dto;

import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AddFood {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private long storeId;
        private List<Food> addingFoods;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private List<Food> registeredFoods;

        public static AddFood.Response fromEntity(Store store){
            return Response.builder()
                    .registeredFoods(store.getFoods())
                    .build();
        }
    }

}
