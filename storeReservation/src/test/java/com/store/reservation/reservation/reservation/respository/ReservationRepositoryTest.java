package com.store.reservation.reservation.reservation.respository;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.repository.MemberRepository;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.reservation.domain.vo.State;
import com.store.reservation.review.domain.Review;
import com.store.reservation.review.repository.ReviewRepository;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.repository.StoreRepository;
import com.store.reservation.store.store.util.implementation.juso.JusoOpenApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = JusoOpenApi.class)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private JusoOpenApi jusoOpenApi;

    List<Member> memberList = new ArrayList<>();
    List<Store> storeList = new ArrayList<>();
    List<Reservation> reservationList = new ArrayList<>();

    @BeforeEach
    void setup() {
        Member manager = Member.builder()

                .name("홍길동")
                .email("xx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();
        Member manager2 = Member.builder()

                .name("고길동")
                .email("x@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Store store = Store.builder()
                .storeName("와이앤웍")
                .manager(manager)
                .starRating(4.0)
                .location(jusoOpenApi.convertToCoordinateFrom("경기도 수원시 권선구 매곡로 88"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .reviews(new ArrayList<Review>())
                .build();

        Store store2 = Store.builder()
                .storeName("스시자카")
                .manager(manager)
                .starRating(4.0)
                .location(jusoOpenApi.convertToCoordinateFrom("경기 수원시 권선구 금호로 83-8 판다팜타워 1층 120호"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .reviews(new ArrayList<Review>())
                .build();

        Member customer = Member.builder()

                .name("홍길동")
                .email("xxx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        Member customer2 = Member.builder()

                .name("고길동")
                .email("xxxx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        storeList.addAll(List.of(store, store2));
        memberList.addAll(List.of(customer, customer2, manager, manager2));
        memberRepository.saveAll(memberList);
        storeRepository.saveAll(storeList);

        LocalDateTime reservedTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0, 0));
        LocalDateTime reservedTime1 = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0));
        LocalDateTime reservedTime2 = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0));
        LocalDateTime reservedTime3 = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0, 0));
        LocalDateTime reservedTime4 = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0));
        LocalDateTime reservedTime5 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(12, 0, 0));
        LocalDateTime reservedTime6 = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(13, 0, 0));

        Reservation reservation1 = Reservation.builder()
                .time(new Time(reservedTime5))
                .store(store)
                .customer(customer)
                .state(new State())
                .build();

        Reservation reservation2 = Reservation.builder()
                .time(new Time(reservedTime6))
                .store(store)
                .customer(customer2)
                .state(new State())
                .build();

        Reservation reservation3 = Reservation.builder()
                .time(new Time(reservedTime1))
                .store(store)
                .customer(customer2)
                .state(new State())
                .build();

        Reservation reservation4 = Reservation.builder()
                .time(new Time(reservedTime2))
                .store(store2)
                .customer(customer2)
                .state(new State())
                .build();

        Reservation reservation5 = Reservation.builder()
                .time(new Time(reservedTime3))
                .store(store)
                .customer(customer2)
                .state(new State())
                .build();

        Reservation reservation6 = Reservation.builder()
                .time(new Time(reservedTime4))
                .store(store2)
                .customer(customer2)
                .state(new State())
                .build();

        Reservation reservation7 = Reservation.builder()
                .time(new Time(reservedTime5))
                .store(store)
                .customer(customer2)
                .state(new State())
                .build();

        reservationList.addAll(List.of(reservation1, reservation2, reservation3, reservation4, reservation5, reservation6, reservation7));
        reservationRepository.saveAll(reservationList);
    }

    @Test
    @DisplayName("오늘 손님 예약 조회 성공")
    void success_findAllByCurrentDateAndStore() {

        // given

        // when
        List<Store> all = storeRepository.findAll();

        List<Reservation> allByCurrentDateAndStore = reservationRepository
                .findAllByCurrentDateAndStore(LocalDate.now(), 1L);

        // then
        assertEquals(allByCurrentDateAndStore.size(), 2);
    }

}