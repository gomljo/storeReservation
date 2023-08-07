package com.store.reservation.store.store.util.implementation.juso;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.util.GeoCoding;
import com.store.reservation.store.store.util.exception.GeoCodingError;
import com.store.reservation.store.store.util.exception.GeoCodingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.store.reservation.store.store.util.exception.GeoCodingError.*;
import static com.store.reservation.store.store.util.implementation.juso.constant.JusoOpenApiConstants.*;

@Component
@RequiredArgsConstructor
@Getter
public class JusoOpenApi implements GeoCoding {

    //  공간 정보 오픈 플랫폼의 오픈 API를 사용
    //  https://www.vworld.kr/dev/v4dv_geocoderguide2_s001.do

    @Value("${spring.openapi.secret}")
    private String apikey;


    /**
     * 도로명 주소를 전달받아 해당하는 좌표로 변환하여
     * 매장의 위치 정보를 갖는 location 객체를 반환
     *
     * @param roadName: 좌표 값으로 변환하고 싶은 도로명 주소
     * @return 매장의 위치 정보를 갖는 location 객체
     */
    @Override
    public Location convertToCoordinateFrom(String roadName) {
        String requestUrl = makeRequestUrl(roadName);

        try {
            URL url = new URL(requestUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String jsonText;

            while ((jsonText = reader.readLine()) != null) {
                stringBuilder.append(jsonText);
            }

            JsonObject apiRequest = (JsonObject) JsonParser.parseString(stringBuilder.toString());
            JsonObject coordinate = parseRequest(apiRequest);

            return new Location(
                    coordinate.get(COORDINATE_Y).getAsDouble(),
                    coordinate.get(COORDINATE_X).getAsDouble(),
                    roadName
            );
        } catch (MalformedURLException e) {
            throw new GeoCodingException(INVALID_URL);
        } catch (IOException e) {
            throw new GeoCodingException(INVALID_RESULT);
        }
    }

    /**
     * 제공받은 open api의 요청 형식에 따른 요청 url 생성
     *
     * @param roadName: 변환하고 싶은 도로명 주소
     * @return 도로명 주소를 포함한 open api 요청 url
     */
    private String makeRequestUrl(String roadName) {

        return String.join(WHITE_SPACE,
                List.of(URL,
                        SERVICE_QUERY, SERVICE,
                        REQUEST_QUERY, REQUEST,
                        FORMAT_QUERY, FORMAT,
                        COORDINATE_SYSTEM_QUERY, COORDINATE_SYSTEM_TYPE,
                        KEY_QUERY, apikey,
                        TYPE_QUERY, SEARCH_TYPE,
                        ADDRESS_QUERY, URLEncoder.encode(roadName, StandardCharsets.UTF_8)));
    }

    /**
     * Gson을 이용하여 응답받은 json을 파싱
     * 만약 요청이 잘못되었다면 GeoCodingException(INVALID_ROAD_NAME) exception이 발생
     *
     * @param request: open api json 응답 객체
     * @return 도로명 주소에 해당하는 좌표 값
     */
    private JsonObject parseRequest(JsonObject request) {
        for (String key : parsingKeys) {
            if (!isParseResultNotNull(request.getAsJsonObject(key))) {
                throw new GeoCodingException(INVALID_ROAD_NAME);
            }
            request = request.getAsJsonObject(key);
        }
        return request;
    }

    /**
     * json 파싱 중 발생하는 jsonObject가 null인지 확인
     *
     * @param parseResult: json 파싱 결과
     * @return null 여부
     */
    private boolean isParseResultNotNull(JsonObject parseResult) {
        if (ObjectUtils.isEmpty(parseResult)) {
            return false;
        }
        return true;
    }

}
