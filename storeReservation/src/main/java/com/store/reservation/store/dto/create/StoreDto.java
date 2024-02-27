package com.store.reservation.store.dto.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.store.reservation.store.domain.vo.food.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime openingHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime closingHour;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime breakTimeStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "kk:mm", timezone = "Asia/Seoul")
    private LocalTime breakTimeEnd;
    private int reservationTimeInterval;
    private List<FoodDto> foodDtoList;

    public Set<Food> getFoodListToSet(){
        return foodDtoList.stream()
                .map(Food::from)
                .collect(Collectors.toSet());
    }

}