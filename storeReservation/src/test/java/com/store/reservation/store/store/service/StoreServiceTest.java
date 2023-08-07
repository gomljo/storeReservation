package com.store.reservation.store.store.service;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.repository.MemberRepository;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.dto.create.CreateStore;
import com.store.reservation.store.store.dto.update.UpdateStore;
import com.store.reservation.store.store.exception.StoreException;
import com.store.reservation.store.store.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.store.reservation.store.store.exception.StoreErrorCode.ALREADY_REGISTERED_STORE_NAME;
import static com.store.reservation.store.store.exception.StoreErrorCode.UPDATE_BEFORE_CREATE_STORE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private StoreService storeService;

    @Test
    @DisplayName("매장 등록 성공")
    void success_createStore() {
        Location location = getLocation("경기도 수원시 권선구 매곡로 100 (금곡동,동성아파트)", 126.956699901, 37.270935524);
        // given
        String storeName = "코딩공작소";
        Store store = getStore(storeName, location);
        Member member = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("xxx@email.com")
                .phoneNumber("010-111-1111")
                .roles(List.of(Role.ROLE_USER.toString()))
                .password("1234234")
                .build();
        given(storeRepository.save(any()))
                .willReturn(store);
        // when

        CreateStore.Request request = getRequest(storeName);
        Store savedStore = storeService.create(request, location, member);
        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

        // then

        verify(storeRepository).save(captor.capture());
        assertEquals(captor.getValue().getStoreName(), savedStore.getStoreName());
        assertEquals(captor.getValue().getLocation(), savedStore.getLocation());
        assertEquals(store.getStoreId(), savedStore.getStoreId());
    }


    @Test
    @DisplayName("매장 등록 실패 - 이미 등록된 매장인 경우")
    void fail_createStore() {
        // given
        String storeName = "코딩공작소";
        Location location = getLocation("경기도 수원시 권선구 매곡로 100 (금곡동,동성아파트)", 126.956699901, 37.270935524);
        Member member = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("xxx@email.com")
                .phoneNumber("010-111-1111")
                .roles(List.of(Role.ROLE_USER.toString()))
                .password("1234234")
                .build();
        given(storeRepository.existsByStoreName(anyString()))
                .willReturn(true);
        // when
        CreateStore.Request request = getRequest(storeName);
        StoreException storeException = assertThrows(StoreException.class,
                () -> storeService.create(request, location, member));

        // then
        assertEquals(storeException.getErrorCode(), ALREADY_REGISTERED_STORE_NAME.toString());
        assertEquals(storeException.getDescription(), ALREADY_REGISTERED_STORE_NAME.getDescription());
    }

    @Test
    @DisplayName("매장 수정 성공")
    void success_updateStore() {

        String storeNameBefore = "코딩공작소";
        String storeNameAfter = "코딩제작소";

        Location location = getLocation("경기도 수원시 권선구 매곡로 100", 126.958, 37.271);
        Location modifiedLocation = getLocation("경기도 수원시 권선구 매곡로 101", 126.900, 37.200);
        // given
        Store findedStore = getStore(storeNameBefore, location);
        Store updatedStore = getStore(storeNameAfter, modifiedLocation);
        given(storeRepository.findByStoreName(anyString()))
                .willReturn(Optional.of(findedStore));
        given(storeRepository.save(any()))
                .willReturn(updatedStore);

        // when
        UpdateStore.Request request = getRequest(storeNameAfter, location);
        Store result = storeService.update(request);
        // then
        assertNotEquals(location, result.getLocation());
        assertNotEquals(findedStore.getStoreName(), result.getStoreName());
        assertEquals(findedStore.getStoreId(), result.getStoreId());
    }

    @Test
    @DisplayName("매장 수정 실패 - 미 등록된 매장을 수정하는 경우")
    void fail_updateStore() {
        Location location = getLocation("경기도 수원시 권선구 매곡로 100", 126.958, 37.271);
        // given

        given(storeRepository.findByStoreName(anyString()))
                .willReturn(Optional.empty());

        // when
        UpdateStore.Request request = UpdateStore.Request.builder()
                .storeName("코딩공작소")
                .location(location)
                .build();
        StoreException storeException = assertThrows(StoreException.class, () -> storeService.update(request));
        // then
        assertEquals(storeException.getErrorCode(), UPDATE_BEFORE_CREATE_STORE.toString());
        assertEquals(storeException.getDescription(), UPDATE_BEFORE_CREATE_STORE.getDescription());
    }

    private static Location getLocation(String roadName, double coordinateX, double coordinateY) {
        return new Location(coordinateX, coordinateY, roadName);
    }

    private static Store getStore(String storeName, Location location) {
        return Store.builder()
                .storeId(1L)
                .storeName(storeName)
                .location(location)
                .foods(new ArrayList<Food>())
                .build();
    }

    private static CreateStore.Request getRequest(String storeName) {
        return CreateStore.Request.builder()
                .memberId(1L)
                .storeName(storeName)
                .build();
    }

    private static UpdateStore.Request getRequest(String storeName, Location location) {
        return UpdateStore.Request.builder()
                .storeName("코딩제작소")
                .location(location)
                .build();
    }
}