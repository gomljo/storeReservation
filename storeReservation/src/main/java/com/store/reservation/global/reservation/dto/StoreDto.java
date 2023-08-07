package com.store.reservation.global.reservation.dto;

import com.store.reservation.review.domain.Review;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {
    private double starRating;
    private String storeName;
    private List<Food> foods;
    private List<Review> reviews;

    public static StoreDto fromEntity(Store store){
        return StoreDto.builder()
                .storeName(store.getStoreName())
                .starRating(store.getStarRating())
                .foods(store.getFoods())
                .reviews(store.getReviews())
                .build();
    }

}
