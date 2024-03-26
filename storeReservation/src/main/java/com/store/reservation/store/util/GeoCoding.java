package com.store.reservation.store.util;

import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;

public interface GeoCoding {


    LocationDto convertToCoordinateFrom(String roadName);
}
