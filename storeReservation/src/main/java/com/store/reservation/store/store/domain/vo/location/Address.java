package com.store.reservation.store.store.domain.vo.location;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;
    private String county;
    private String district;
    private String detailAddress;
    private String floor;

}
