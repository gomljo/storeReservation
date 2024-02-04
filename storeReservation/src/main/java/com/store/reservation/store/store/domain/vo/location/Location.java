package com.store.reservation.store.store.domain.vo.location;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class Location {

    @Embedded
    private Coordinate coordinate;
    @Embedded
    private Address address;

}
