package com.store.reservation.store.store.domain.vo;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class Location {

    private double lat;
    private double lnt;
    private String roadName;

    public Location() {

    }
    public Location(double lat, double lnt, String roadName) {
        this.lat = lat;
        this.lnt = lnt;
        this.roadName = roadName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location location = (Location) o;
        return Double.compare(location.lat, lat) == 0
                && Double.compare(location.lnt, lnt) == 0
                && Objects.equals(roadName, location.roadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lnt, roadName);
    }
}
