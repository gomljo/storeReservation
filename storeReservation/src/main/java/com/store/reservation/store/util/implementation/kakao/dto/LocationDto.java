package com.store.reservation.store.util.implementation.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LocationDto {
    private Double longitude;
    private Double latitude;
    private String city;
    private String county;
    private String district;
    private String roadName;

    public static LocationDto from(LocalApiDto localApiDto){
        return LocationDto.builder()
                .roadName(localApiDto.getAddressName())
                .latitude(localApiDto.getLatitude())
                .longitude(localApiDto.getLongitude())
                .city(localApiDto.getCity())
                .county(localApiDto.getCounty())
                .district(localApiDto.getDistrict())
                .build();
    }
}
