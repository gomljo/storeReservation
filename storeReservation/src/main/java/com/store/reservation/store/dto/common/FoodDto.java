package com.store.reservation.store.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FoodDto {
    private long price;
    private String description;
    private String category;
    private String name;
}
