package com.store.reservation.store.domain.vo.location;

import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
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
public class Location {
    private String city;
    private String county;
    private String district;
    private String roadName;
    private double lat;
    private double lnt;

    public static Location createBy(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLatitude())
                .lnt(locationDto.getLongitude())
                .city(locationDto.getCity())
                .county(locationDto.getCounty())
                .district(locationDto.getDistrict())
                .roadName(locationDto.getRoadName())
                .build();
    }
}
