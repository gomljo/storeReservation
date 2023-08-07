package com.store.reservation.reservation.reservation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.reservation.annotation.UserTest;
import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.service.MemberService;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.reservation.domain.model.type.ApprovalState;
import com.store.reservation.reservation.reservation.domain.vo.State;
import com.store.reservation.reservation.reservation.domain.vo.Time;
import com.store.reservation.reservation.reservation.dto.UpdateApproval;
import com.store.reservation.reservation.reservation.service.ReservationModuleService;
import com.store.reservation.store.store.controller.StoreController;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.domain.vo.Location;
import com.store.reservation.store.store.util.implementation.juso.JusoOpenApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ReservationController.class)
@Import(JusoOpenApi.class)
@UserTest
class ReservationControllerTest {

    @MockBean
    private ReservationModuleService reservationModuleService;

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDateTime currentTime;

    @BeforeEach
    void timeSetup(){
        currentTime = LocalDateTime.now().withNano(0);
    }

    @Test
    @DisplayName("예약 승인 상태 업데이트 성공")
    void success_updateReservationApprovalState() throws Exception {

        // given
        Reservation reservation = setupReservation();
        UpdateApproval.Request request = new UpdateApproval.Request(ApprovalState.APPROVAL);
        given(reservationModuleService.updateApprovalState(anyLong(), any()))
                .willReturn(reservation);
        // then
        mockMvc.perform(put("/reservation/update/approval?reservationId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                        .with(user("홍길동").password("1231455").roles("PARTNER"))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approvalState").value(ApprovalState.APPROVAL.toString()))
                .andExpect(jsonPath("$.reservationId").value(reservation.getReservationId()))
                .andExpect(jsonPath("$.reservationTime").value(currentTime.toString()))
                .andExpect(jsonPath("$.storeName").value("와이앤웍"));

    }

    private Reservation setupReservation(){
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

        State stateApprovedAndReserved = new State();
        stateApprovedAndReserved.changeApprovalState(ApprovalState.APPROVAL);
        stateApprovedAndReserved.changeReservationStateToReserved();

        return Reservation.builder()
                .reservationId(1L)
                .customer(customer)
                .state(stateApprovedAndReserved)
                .store(store)
                .time(new Time(currentTime))
                .build();

    }
}