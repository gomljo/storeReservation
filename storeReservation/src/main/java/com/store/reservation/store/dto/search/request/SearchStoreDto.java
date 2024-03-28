package com.store.reservation.store.dto.search.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchStoreDto {
    private Double latitude;
    private Double longitude;
    private Double radius;
}
