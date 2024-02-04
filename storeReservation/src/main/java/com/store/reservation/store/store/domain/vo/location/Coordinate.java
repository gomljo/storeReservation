package com.store.reservation.store.store.domain.vo.location;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
public class Coordinate {

    private double lat;
    private double lnt;
    private String roadName;

    public Coordinate() {

    }
    public Coordinate(double lat, double lnt, String roadName) {
        this.lat = lat;
        this.lnt = lnt;
        this.roadName = roadName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate coordinate = (Coordinate) o;
        return Double.compare(coordinate.lat, lat) == 0
                && Double.compare(coordinate.lnt, lnt) == 0
                && Objects.equals(roadName, coordinate.roadName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lnt, roadName);
    }
}
