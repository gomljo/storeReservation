package com.store.reservation.store.dto.common;

import com.store.reservation.store.domain.vo.food.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class StoreDto {
    private String storeName;
    private String roadName;
    private TimeDto timeDto;
    private List<FoodDto> foodDtoList;

    public Set<Food> getFoodListToSet(){
        return foodDtoList.stream()
                .map(Food::from)
                .collect(Collectors.toSet());
    }

}