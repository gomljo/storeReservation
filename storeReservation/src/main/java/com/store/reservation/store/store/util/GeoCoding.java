package com.store.reservation.store.store.util;

import com.store.reservation.store.store.domain.vo.Location;

public interface GeoCoding {


    Location convertToCoordinateFrom(String roadName);
}
