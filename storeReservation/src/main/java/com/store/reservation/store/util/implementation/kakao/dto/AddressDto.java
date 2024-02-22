package com.store.reservation.store.util.implementation.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressDto {
    @JsonProperty("region_1depth_name")
    private String city;
    @JsonProperty("region_2depth_name")
    private String county;
    @JsonProperty("region_3depth_name")
    private String district;
    @JsonProperty("region_3depth_h_name")
    private String detailAddress;
    @JsonProperty("address_name")
    private String roadName;


}
