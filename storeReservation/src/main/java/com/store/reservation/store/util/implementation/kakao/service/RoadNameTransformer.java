package com.store.reservation.store.util.implementation.kakao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.reservation.store.util.GeoCoding;
import com.store.reservation.store.util.implementation.kakao.dto.LocalApiDto;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import com.store.reservation.store.util.implementation.kakao.exception.LocalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static com.store.reservation.store.util.implementation.kakao.constant.UrlConstants.*;
import static com.store.reservation.store.util.implementation.kakao.exception.LocalErrorCode.CANNOT_GET_API_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadNameTransformer implements GeoCoding {

    @Value("${spring.kakao-local.api-key}")
    private String adminAPIKey;

    private final RestTemplate restTemplate;

    @Override
    public LocationDto convertToCoordinateFrom(String roadName) {
        HttpEntity<String> httpEntity = new HttpEntity<>(getHeaders()); //엔티티로 만들기
        URI targetUrl = UriComponentsBuilder
                .fromUriString(LOCAL_API_URL)
                .queryParam(PARAMETER, roadName)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        //API 호출
        ResponseEntity<String> apiResponse;
        LocalApiDto localApiDto;
        try {
            apiResponse = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, String.class);
            localApiDto  = new ObjectMapper().readerFor(LocalApiDto.class).readValue(apiResponse.getBody());
            log.info(String.valueOf(localApiDto.getLatitude()));
            return LocationDto.from(localApiDto);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new LocalException(CANNOT_GET_API_RESPONSE);
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = KAKAO_PREFIX + adminAPIKey;

        httpHeaders.set(HEADER, auth);
        httpHeaders.set(CONTENT_TYPE_TITLE, CONTENT_TYPE);

        return httpHeaders;
    }
}
