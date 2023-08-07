package com.store.reservation.store.store.util;

import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.util.exception.GeoCodingException;
import com.store.reservation.store.store.util.implementation.juso.JusoOpenApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static com.store.reservation.store.store.util.exception.GeoCodingError.INVALID_ROAD_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@SpringBootTest
class JusoOpenApiTest {
    @Autowired
    private JusoOpenApi jusoOpenApi;

    @Test
    @DisplayName("주소 변환 성공 - 도, 시, (군, 구), 도로명")
    void success_transform_all(){

        // given
        String roadName = "경기도 수원시 권선구 매곡로 100";
        // when
        Location location = jusoOpenApi.convertToCoordinateFrom(roadName);
        // then
        assertEquals(location.getRoadName(), roadName);
    }

    @Test
    @DisplayName("주소 변환 성공 - 시, 군, 구, 도로명")
    void success_transform_except_province(){

        // given
        String roadName = "수원시 권선구 매곡로 100";
        // when
        Location location = jusoOpenApi.convertToCoordinateFrom(roadName);
        // then
        assertEquals(location.getRoadName(), roadName);
    }

    @Test
    @DisplayName("주소 변환 성공 - 군, 구, 도로명")
    void success_transform_except_provinceAndCity(){

        // given
        String roadName = "권선구 매곡로 100";
        // when
        Location location = jusoOpenApi.convertToCoordinateFrom(roadName);
        // then
        assertEquals(location.getRoadName(), roadName);
    }

    @Test
    @DisplayName("주소 변환 성공 - 군, 구, 도로명")
    void success_transform_except_provinceAndCityAndDistrict(){

        // given
        String roadName = "매곡로 100";
        // when
        Location location = jusoOpenApi.convertToCoordinateFrom(roadName);

        // then
        assertEquals(location.getRoadName(), roadName);
    }

    @Test
    @DisplayName("주소 변환 실패 - 국내에 존재하지 않는 도로명인 경우")
    void fail_convert_DoesNotExistsInKorea(){

        // given
        String roadName = "뉴욕시 퀸즈파크 애비뉴";
        // when
        GeoCodingException exception = assertThrows(GeoCodingException.class, () -> jusoOpenApi.convertToCoordinateFrom(roadName));
        // then
        assertEquals(exception.getDescription(), INVALID_ROAD_NAME.getDescription());
    }
}