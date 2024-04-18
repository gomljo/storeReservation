package com.store.reservation.store.service;


import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.domain.vo.food.Food;
import com.store.reservation.store.domain.vo.location.Location;
import com.store.reservation.store.domain.vo.operating.OperatingHours;
import com.store.reservation.store.dto.common.StoreDto;
import com.store.reservation.store.exception.StoreErrorCode;
import com.store.reservation.store.exception.StoreRuntimeException;
import com.store.reservation.store.repository.jpa.StoreRepository;
import com.store.reservation.store.repository.queryDsl.StoreSearchRepository;
import com.store.reservation.store.util.GeoCoding;
import com.store.reservation.store.util.implementation.kakao.dto.LocationDto;
import com.store.reservation.store.util.implementation.kakao.exception.LocalErrorCode;
import com.store.reservation.store.util.implementation.kakao.exception.LocalRuntimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {
    private static final MemberInformation VISITOR = null;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreSearchRepository storeSearchRepository;
    @Mock
    private GeoCoding geoCoding;
    @InjectMocks
    private StoreService storeService;


    @Test
    @DisplayName("매장 정보 생성 실패 - 이미 매장을 등록한 경우")
    void create_fail_when_already_created() {
        given(storeRepository.existsStoreByMemberInformation(any()))
                .willReturn(true);
        MemberInformation managerInformation = getMemberInformation(Role.ROLE_PARTNER);
        StoreDto storeDto = StoreDto.builder()
                .storeName("이미 등록한 매장 정보 이름")
                .build();
        StoreRuntimeException storeException = assertThrows(StoreRuntimeException.class, () -> storeService.create(storeDto, managerInformation));
        assertEquals(StoreErrorCode.ALREADY_REGISTERED.toString(), storeException.getErrorCode());
    }

    @Test
    @DisplayName("매장 정보 생성 실패 - 매장 주소를 잘못 입력한 경우")
    void create_fail_when_enter_wrong_road_name() {
        given(storeRepository.existsStoreByMemberInformation(any()))
                .willReturn(false);
        given(geoCoding.convertToCoordinateFrom(anyString()))
                .willThrow(new LocalRuntimeException(LocalErrorCode.CANNOT_GET_API_RESPONSE));
        MemberInformation memberInformation = getMemberInformation(Role.ROLE_PARTNER);
        StoreDto storeDto = StoreDto.builder()
                .storeName("이미 등록한 매장 정보 이름")
                .roadName("워싱턴주 홀란드")
                .build();
        LocalRuntimeException localException = assertThrows(LocalRuntimeException.class, () -> storeService.create(storeDto, memberInformation));
        assertEquals(LocalErrorCode.CANNOT_GET_API_RESPONSE.name(), localException.getErrorCode());
    }

    @Test
    @DisplayName("매장 정보 수정 실패 - 본인의 매장이 아닌 경우")
    void update_fail_when_not_my_store() {
        Long storeId = 2L;
        MemberInformation managerInformation = getMemberInformation(Role.ROLE_PARTNER);
        StoreDto updateStoreDto = StoreDto.builder()
                .storeName("본인 것이 아닌 매장의 이름")
                .roadName("경기도 성남시 분당구 판교역로 166")
                .build();
        given(geoCoding.convertToCoordinateFrom(anyString()))
                .willReturn(LocationDto.builder()
                        .city("경기도")
                        .city("성남시")
                        .county("분당구")
                        .district("백현동")
                        .roadName("경기도 성남시 분당구 판교역로 166")
                        .latitude(127.3453453)
                        .longitude(134.1232413)
                        .build());
        given(storeRepository.findByIdAndMemberInformation(storeId, managerInformation))
                .willThrow(new StoreRuntimeException(StoreErrorCode.UPDATE_BEFORE_CREATE_STORE));

        StoreRuntimeException storeException = assertThrows(StoreRuntimeException.class, () -> storeService.update(updateStoreDto, storeId, managerInformation));
        assertEquals(StoreErrorCode.UPDATE_BEFORE_CREATE_STORE.toString(), storeException.getErrorCode());
    }

    @Test
    @DisplayName("매장 정보 수정 실패 - 수정한 주소가 올바른 주소가 아닌 경우")
    void update_fail_when_enter_wrong_road_name() {
        Long storeId = 1L;
        MemberInformation memberInformation = getMemberInformation(Role.ROLE_PARTNER);
        StoreDto storeDto = StoreDto.builder()
                .storeName("이전에 등록한 매장 이름")
                .roadName("워싱턴주 홀란드")
                .build();
        given(geoCoding.convertToCoordinateFrom(anyString()))
                .willThrow(new LocalRuntimeException(LocalErrorCode.CANNOT_GET_API_RESPONSE));

        LocalRuntimeException localException = assertThrows(LocalRuntimeException.class, () -> storeService.update(storeDto, storeId, memberInformation));
        assertEquals(LocalErrorCode.CANNOT_GET_API_RESPONSE.name(), localException.getErrorCode());
    }

    @Test
    @DisplayName("매장 세부 정보 조회 성공 - 비회원과 고객이 조회를 요청할 때")
    void searchStoreByCustomer() {
        Long storeId = 1L;
        Store expectedStore = Store.builder()
                .id(1L)
                .storeName("나의 매장")
                .numberOfReviews(100L)
                .numberOfReservationPerTime(10)
                .starCount(5)
                .operatingHours(OperatingHours.builder()
                        .openingHours(LocalTime.of(9, 0))
                        .closingHours(LocalTime.of(22, 0))
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(15, 0))
                        .build())
                .location(Location.builder()
                        .city("경기도")
                        .city("성남시")
                        .county("분당구")
                        .district("백현동")
                        .roadName("경기도 성남시 분당구 판교역로 166")
                        .lat(127.3453453)
                        .lnt(134.1232413)
                        .build())
                .foods(new HashSet<>(List.of(Food.builder()
                        .category("한식")
                        .description("맛있는 김치찌개")
                        .name("김치찌개")
                        .price(10000L)
                        .build())))
                .build();
        given(storeRepository.findById(anyLong()))
                .willReturn(Optional.of(expectedStore));

        Store actualStore = storeService.searchStoreByCustomer(storeId);
        verify(storeRepository, times(1)).findById(anyLong());

        assertAll(
                () -> assertEquals(actualStore.getId(), expectedStore.getId()),
                () -> assertEquals(actualStore.getStoreName(), expectedStore.getStoreName()),
                () -> assertEquals(actualStore.getLocation(), expectedStore.getLocation()),
                () -> assertEquals(actualStore.getFoods(), expectedStore.getFoods()),
                () -> assertEquals(actualStore.getNumberOfReviews(), expectedStore.getNumberOfReviews()),
                () -> assertEquals(actualStore.getOperatingHours(), expectedStore.getOperatingHours()),
                () -> assertEquals(actualStore.getStarCount(), expectedStore.getStarCount()),
                () -> assertEquals(actualStore.getNumberOfReservationPerTime(), expectedStore.getNumberOfReservationPerTime())
        );

    }

    @Test
    @DisplayName("매장 세부 정보 조회 실패 - 자신의 매장이 아닌 경우")
    void searchStoreByOwner() {
        Long storeId = 2L;
        MemberInformation otherStoreManager = getMemberInformation(Role.ROLE_PARTNER);
        given(storeRepository.findByIdAndMemberInformation(anyLong(), any()))
                .willThrow(new StoreRuntimeException(StoreErrorCode.STORE_NOT_FOUND));

        StoreRuntimeException storeException = assertThrows(StoreRuntimeException.class, () -> storeService.searchStoreByOwner(otherStoreManager, storeId));
        assertEquals(StoreErrorCode.STORE_NOT_FOUND.toString(), storeException.getErrorCode());
    }

    private static MemberInformation getMemberInformation(Role role) {
        switch (role) {
            case ROLE_USER:
                return MemberInformation.builder()
                        .email("customer1@gmail.com")
                        .roles(List.of(Role.ROLE_USER.toString()))
                        .id(1L)
                        .password("11111")
                        .build();
            case ROLE_PARTNER:
                return MemberInformation.builder()
                        .email("manager1@gmail.com")
                        .roles(List.of(Role.ROLE_PARTNER.toString()))
                        .id(2L)
                        .password("123456")
                        .build();
            default:
                return VISITOR;
        }
    }
}
