package com.store.reservation.store.domain.vo.food;

import com.store.reservation.store.dto.create.FoodDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Food {

    private long price;
    private String description;
    private String category;

    public static Food from(FoodDto foodDto) {
        return Food.builder()
                .category(foodDto.getCategory())
                .price(foodDto.getPrice())
                .description(foodDto.getDescription())
                .build();
    }
}