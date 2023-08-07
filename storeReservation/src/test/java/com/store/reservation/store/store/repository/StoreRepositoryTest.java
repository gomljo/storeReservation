package com.store.reservation.store.store.repository;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.repository.MemberRepository;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.util.implementation.juso.JusoOpenApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = JusoOpenApi.class)
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JusoOpenApi jusoOpenApi;

    Location currentLocation;
    Pageable pagingRequest;
    @BeforeEach
    void setup() {
        pagingRequest = PageRequest.of(0, 100);
        currentLocation = new Location(126.956699901,
                37.270935524, "경기도 수원시 권선구 매곡로 100");

        Member manager1 = Member.builder()
                .name("홍길동")
                .email("xxx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Member manager2 = Member.builder()
                .name("김길동")
                .email("xxxx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Member manager3 = Member.builder()
                .name("강길동")
                .email("xxxxx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Member manager4 = Member.builder()
                .name("고길동")
                .email("xx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        memberRepository.saveAll(List.of(manager1, manager2, manager3, manager4));

        Store store1 = Store.builder()
                .storeName("와이앤웍")
                .manager(manager1)
                .starRating(4.0)
                .location(jusoOpenApi.convertToCoordinateFrom("경기도 수원시 권선구 매곡로 88"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .build();
        Store store2 = Store.builder()
                .storeName("올리앤")
                .manager(manager2)
                .starRating(4.5)
                .location(jusoOpenApi.convertToCoordinateFrom("경기 수원시 권선구 서수원로 533-39 1층"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .build();
        Store store3 = Store.builder()
                .storeName("스시자카")
                .manager(manager3)
                .starRating(4.5)
                .location(jusoOpenApi.convertToCoordinateFrom("경기 수원시 권선구 금호로 83-8 판다팜타워 1층 120호"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .build();
        Store store4 = Store.builder()
                .storeName("가마치통닭 호매실점")
                .manager(manager4)
                .starRating(0.0)
                .location(jusoOpenApi.convertToCoordinateFrom("경기 수원시 권선구 서수원로523번길 20-1 1층 101호"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .build();
        storeRepository.saveAll(List.of(store1, store2, store3, store4));
    }


    @Test
    @DisplayName("주어진 반경 내 음식점 조회 성공 - 가까운 거리 순서")
    void success_searchByRadiusDistanceASC() {

        // when
        List<Store> stores = storeRepository.searchStoreByRadiusDistanceASC(
                currentLocation.getLnt(), currentLocation.getLat(), 1000, pagingRequest);

        // then
        List<String> storeNames = new ArrayList<>();
        for (Store store : stores) {
            storeNames.add(store.getStoreName());
        }
        assertEquals(storeNames, List.of("올리앤", "와이앤웍", "가마치통닭 호매실점", "스시자카"));

    }

    @Test
    @DisplayName("주어진 반경 내 음식점 조회 성공 - 음식점 이름 오름차순")
    void success_searchByRadiusAlphabeticASC() {

        // given

        // when
        List<Store> stores = storeRepository.searchStoreByRadiusAlphabeticASC(
                currentLocation.getLnt(), currentLocation.getLat(), 1000, pagingRequest);

        // then
        List<String> storeNames = new ArrayList<>();
        for (Store store : stores) {
            storeNames.add(store.getStoreName());
        }
        assertEquals(storeNames, List.of("가마치통닭 호매실점", "스시자카", "올리앤", "와이앤웍"));

    }

    @Test
    @DisplayName("주어진 반경 내 음식점 조회 성공 - 별점이 높은 순서, 만약 별점이 같다면 이름 순")
    void success_searchByRadiusStarRatingASC() {
        // when
        List<Store> stores = storeRepository.searchStoreByRadiusStarRatingDESC(
                currentLocation.getLnt(), currentLocation.getLat(), 1000, pagingRequest);

        // then
        List<String> storeNames = new ArrayList<>();
        for (Store store : stores) {
            storeNames.add(store.getStoreName());
        }
        assertEquals(storeNames, List.of("스시자카", "올리앤", "와이앤웍", "가마치통닭 호매실점"));
    }


}