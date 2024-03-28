package com.store.reservation.store.util.implementation.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

import static com.store.reservation.store.util.implementation.kakao.constant.LocalApiJsonParsingConstants.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonIgnoreProperties(value = {META_INFORMATION})
public class LocalApiDto {

    private String addressName;
    private String city;
    private String county;
    private String district;
    private Double latitude;
    private Double longitude;

    @SuppressWarnings("unchecked")
    @JsonProperty(API_RESPONSE_LIST)
    private void unpackNested(List<HashMap<String, Object>> documentContents) {
        HashMap<String, Object> content = documentContents.get(FIRST_ELEMENT);
        HashMap<String, Object> roadAddress = (HashMap<String, Object>) content.get(ROAD_ADDRESS);
        this.addressName = String.valueOf(roadAddress.get(ADDRESS_NAME));
        this.city = String.valueOf(roadAddress.get(CITY));
        this.county = String.valueOf(roadAddress.get(COUNTY));
        this.district = String.valueOf(roadAddress.get(DISTRICT));
        this.latitude = Double.valueOf(String.valueOf(roadAddress.get(LATITUDE)));
        this.longitude = Double.valueOf(String.valueOf(roadAddress.get(LONGITUDE)));
    }
}
