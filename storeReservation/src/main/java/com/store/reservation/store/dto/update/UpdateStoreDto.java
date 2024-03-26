package com.store.reservation.store.dto.update;

import com.store.reservation.store.domain.vo.food.Food;
import com.store.reservation.store.dto.common.FoodDto;
import com.store.reservation.store.dto.common.TimeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStoreDto {
    private String storeName;
    private String roadName;
    private TimeDto timeDto;
    private List<FoodDto> foodDtoList;

    public Set<Food> getFoodListToSet() {
        return foodDtoList.stream()
                .map(Food::from)
                .collect(Collectors.toSet());
    }
}
