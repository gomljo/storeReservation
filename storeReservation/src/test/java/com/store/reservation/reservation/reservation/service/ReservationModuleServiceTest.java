package com.store.reservation.reservation.reservation.service;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.domain.model.type.ReservationState;
import com.store.reservation.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.reservation.exception.ReservationException;
import com.store.reservation.reservation.reservation.respository.ReservationRepository;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.domain.vo.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.store.reservation.reservation.reservation.exception.ReservationError.ALREADY_RESERVED;
import static com.store.reservation.reservation.reservation.exception.ReservationError.NO_SUCH_RESERVATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationModuleServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationModuleService reservationModuleService;

    @Test
    @DisplayName("예약 등록 성공")
    void success_registerReservation() {

        // given
        Member customer = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        Member manager = Member.builder()
                .memberId(2L)
                .name("고길동")
                .email("ggildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Store store = Store.builder()
                .storeId(1L)
                .storeName("와이앤웍")
                .location(new Location(126, 30, "우리나라 어딘가"))
                .foods(new ArrayList<>())
                .reservations(new ArrayList<>())
                .manager(manager)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customer(customer)
                .state(new State())
                .store(store)
                .time(new Time(LocalDateTime.now()))
                .build();

        given(reservationRepository.save(any()))
                .willReturn(reservation);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        // when

        Reservation registeredReservation = reservationModuleService.registerReservation(reservation);

        // then

        verify(reservationRepository, times(1)).save(captor.capture());
        assertEquals(captor.getValue().getStore(), registeredReservation.getStore());
        assertEquals(captor.getValue().getTime(), registeredReservation.getTime());
        assertEquals(captor.getValue().getReservedState(), registeredReservation.getReservedState());
        assertEquals(captor.getValue().getCustomer(), registeredReservation.getCustomer());
    }

    @Test
    @DisplayName("예약 등록 실패 - 이미 예약된 시간을 예약")
    void fail_registerReservation_AlreadyReserved() {
        Member customer = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        Member manager = Member.builder()
                .memberId(2L)
                .name("고길동")
                .email("ggildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Store store = Store.builder()
                .storeId(1L)
                .storeName("와이앤웍")
                .location(new Location(126, 30, "우리나라 어딘가"))
                .foods(new ArrayList<>())
                .reservations(new ArrayList<>())
                .manager(manager)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customer(customer)
                .state(new State())
                .store(store)
                .time(new Time(LocalDateTime.now()))
                .build();
        // given
        given(reservationRepository.existsReservationByTime(any()))
                .willReturn(true);
        // when
        ReservationException reservationException = assertThrows(ReservationException.class, () -> reservationModuleService.registerReservation(reservation));
        // then
        assertEquals(reservationException.getErrorCode(), ALREADY_RESERVED.toString());
        assertEquals(reservationException.getDescription(), ALREADY_RESERVED.getDescription());
    }

    @Test
    @DisplayName("승인 상태 변경 성공")
    void success_updateApproval() {
        Member customer = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        Member manager = Member.builder()
                .memberId(2L)
                .name("고길동")
                .email("ggildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Store store = Store.builder()
                .storeId(1L)
                .storeName("와이앤웍")
                .location(new Location(126, 30, "우리나라 어딘가"))
                .foods(new ArrayList<>())
                .reservations(new ArrayList<>())
                .manager(manager)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customer(customer)
                .state(new State())
                .store(store)
                .time(new Time(LocalDateTime.now()))
                .build();

        State stateApprovedAndReserved = new State();
        stateApprovedAndReserved.changeApprovalState(ApprovalState.APPROVAL);
        stateApprovedAndReserved.changeReservationStateToReserved();

        Reservation updatedReservation = Reservation.builder()
                .reservationId(1L)
                .customer(customer)
                .state(stateApprovedAndReserved)
                .store(store)
                .time(new Time(LocalDateTime.now()))
                .build();
        // given
        given(reservationRepository.findById(any()))
                .willReturn(Optional.of(reservation));
        given(reservationRepository.save(any()))
                .willReturn(updatedReservation);
        // when
        Reservation updated = reservationModuleService.updateApprovalState(1L, ApprovalState.APPROVAL);

        // then
        assertEquals(updated.getState().getApprovalState(), ApprovalState.APPROVAL);
        assertEquals(updated.getState().getReservationState(), ReservationState.RESERVED);
    }

    @Test
    @DisplayName("승인 상태 변경 성공")
    void fail_updateApproval() {

        // given
        given(reservationRepository.findById(any()))
                .willReturn(Optional.empty());

        // when
        ReservationException reservationException = assertThrows(ReservationException.class,
                () -> reservationModuleService.updateApprovalState(
                        1L, ApprovalState.APPROVAL));

        // then
        assertEquals(reservationException.getErrorCode(), NO_SUCH_RESERVATION.toString());
        assertEquals(reservationException.getDescription(), NO_SUCH_RESERVATION.getDescription());
    }


    @Test
    @DisplayName("당일 요청된 예약 리스트 조회 성공 - 상점에 들어온 요청이 있는 경우")
    void success_searchReservationList() {

        // given

        List<Reservation> reservationList = setupReservationList();

        given(reservationRepository.findAllByCurrentDateAndStore(any(), anyLong()))
                .willReturn(reservationList);

        // when
        List<Reservation> searchResult = reservationModuleService.searchByCurrentDate(1L);

        // then
        assertEquals(reservationList.size(), searchResult.size());
        assertArrayEquals(reservationList.toArray(), searchResult.toArray());
     }

    private List<Reservation> setupReservationList(){
        Member customer = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("hgildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        Member customer2 = Member.builder()
                .memberId(2L)
                .name("고길동")
                .email("ggildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        Member manager = Member.builder()
                .memberId(2L)
                .name("고길동")
                .email("ggildong@gmail.com")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        Store store = Store.builder()
                .storeId(1L)
                .storeName("와이앤웍")
                .location(new Location(126, 30, "우리나라 어딘가"))
                .foods(new ArrayList<>())
                .reservations(new ArrayList<>())
                .manager(manager)
                .build();

        Reservation reservation = Reservation.builder()
                .reservationId(1L)
                .customer(customer)
                .state(new State())
                .store(store)
                .time(new Time(LocalDateTime.now().minusMinutes(30)))
                .build();

        State stateApprovedAndReserved = new State();
        stateApprovedAndReserved.changeApprovalState(ApprovalState.APPROVAL);
        stateApprovedAndReserved.changeReservationStateToReserved();

        Reservation reservation2 = Reservation.builder()
                .reservationId(2L)
                .customer(customer)
                .state(stateApprovedAndReserved)
                .store(store)
                .time(new Time(LocalDateTime.now()))
                .build();
        return List.of(reservation, reservation2);
    }


    @Test
    @DisplayName("당일 요청된 예약 리스트 조회 성공 - 상점에 들어온 요청이 없는 경우")
    void success_searchReservationList_NoResult() {

        // given
        List<Reservation> reservationList = new ArrayList<>();
        given(reservationRepository.findAllByCurrentDateAndStore(any(), anyLong()))
                .willReturn(reservationList);

        // when
        List<Reservation> searchResult = reservationModuleService.searchByCurrentDate(1L);

        // then
        assertEquals(searchResult.size(), 0);
    }

}