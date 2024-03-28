package com.store.reservation.store.util.implementation.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CoordinateDto {
    @JsonProperty("x")
    private double latitude;
    @JsonProperty("y")
    private double longitude;
}
