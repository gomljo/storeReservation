package com.store.reservation.store.store.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.reservation.annotation.UserTest;
import com.store.reservation.config.security.SecurityConfig;
import com.store.reservation.exception.GlobalErrorCode;
import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.security.CustomUserDetailService;
import com.store.reservation.member.service.MemberServiceImpl;
import com.store.reservation.reservation.reservation.domain.model.Reservation;
import com.store.reservation.store.food.domain.Food;
import com.store.reservation.store.store.domain.model.Store;
import com.store.reservation.store.store.dto.create.CreateStore;
import com.store.reservation.store.store.service.StoreService;
import com.store.reservation.store.store.util.implementation.juso.JusoOpenApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StoreController.class)
@Import({JusoOpenApi.class, SecurityConfig.class})
@UserTest
class StoreControllerTest {

    @MockBean
    private StoreService storeService;

    @MockBean
    private MemberServiceImpl memberServiceImpl;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JusoOpenApi jusoOpenApi;

    private Member manager;
    private Store store;


    private CreateStore.Request request;

    @BeforeEach
    void setup() {
        request = CreateStore.Request.builder()
                .memberId(1L)
                .storeName("와이앤웍")
                .roadName("경기도 수원시 권선구 매곡로 88")
                .build();

        manager = Member.builder()
                .memberId(1L)
                .name("홍길동")
                .email("xxx@email.com")
                .phoneNumber("010-111-1111")
                .password("1234567")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();

        store = Store.builder()
                .storeName("와이앤웍")
                .manager(manager)
                .starRating(4.0)
                .location(jusoOpenApi.convertToCoordinateFrom("경기도 수원시 권선구 매곡로 88"))
                .foods(new ArrayList<Food>())
                .reservations(new ArrayList<Reservation>())
                .build();
    }


    @Test
    @WithMockUser(roles = "PARTNER")
    @DisplayName("매장 등록 성공")
    void success_registerStore() throws Exception {
        // given
        given(memberServiceImpl.searchBy(anyLong()))
                .willReturn(manager);
        given(storeService.create(any(), any(), any()))
                .willReturn(store);

        // then
        mockMvc.perform(post("/store/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName").value(store.getStoreName()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("매장 등록 실패 - 권한 없음")
    void fail_registerStore_NoAuthority() throws Exception {

        // given
        given(memberServiceImpl.searchBy(anyLong()))
                .willReturn(manager);
        given(storeService.create(any(), any(), any()))
                .willReturn(store);

        // then
        mockMvc.perform(post("/store/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(GlobalErrorCode.INTERNAL_SERVER_ERROR.toString()));
    }

}