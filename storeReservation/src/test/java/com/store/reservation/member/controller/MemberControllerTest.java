package com.store.reservation.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.reservation.config.TestSecurityConfig;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.exception.MemberRuntimeException;
import com.store.reservation.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Stream;

import static com.store.reservation.member.exception.MemberError.ALREADY_JOINED_CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestSecurityConfig.class)
@WebMvcTest(value = MemberController.class)
class MemberControllerTest {
    @MockBean
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("회원 가입")
    class signupTest {
        @Test
        @DisplayName("성공")
        void success_signup() throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww!w")
                    .username("개발자")
                    .phoneNumber("010-0000-0000")
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));
            //then
            perform.andExpectAll(
                    status().isOk(),
                    jsonPath("$.status").value("SUCCESS")
            );
        }

        @Test
        @DisplayName("실패")
        void fail_signup_when_email_already_exist() throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww")
                    .username("개발자")
                    .phoneNumber("010-0000-0000")
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            MemberRuntimeException memberRuntimeException = new MemberRuntimeException(ALREADY_JOINED_CUSTOMER);
            doThrow(memberRuntimeException).when(memberService).register(any());

            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));
            //then
            perform.andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR")
            );
        }


        @ParameterizedTest
        @ValueSource(strings = {"dev%gmail.com", "dev@gmail"})
        @DisplayName("실패 - 이메일 주소 형식이 올바르지 않은 경우")
        void fail_signup_when_email_format_is_invalid(String email) throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email(email)
                    .password("12345Qww")
                    .username("개발자")
                    .phoneNumber("010-0000-0000")
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.message").value(HttpStatus.BAD_REQUEST.toString())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"개발자1", "길동@", "11111", "@#!@$1234"})
        @DisplayName("실패 - 이름이 올바르지 않은 경우")
        void fail_signup_when_name_is_invalid(String name) throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww")
                    .username(name)
                    .phoneNumber("010-0000-0000")
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.message").value(HttpStatus.BAD_REQUEST.toString())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"010-333-4444", "asdfasdf", "asdfㄳㄴㅌ", "333-4444", "01-3333-4444", "010-4444-333"})
        @DisplayName("실패 - 전화번호 양식이 올바르지 않은 경우")
        void fail_signup_when_phone_number_format_is_invalid(String phoneNumber) throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww")
                    .username("개발자")
                    .phoneNumber(phoneNumber)
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.message").value(HttpStatus.BAD_REQUEST.toString())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345678, 1234567Q", "1234567q", "12345Qq!"})
        @DisplayName("실패 - 비밀번호 양식이 올바르지 않은 경우")
        void fail_signup_when_password_format_is_invalid(String phoneNumber) throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww")
                    .username("개발자")
                    .phoneNumber(phoneNumber)
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.message").value(HttpStatus.BAD_REQUEST.toString())
            );
        }


        @ParameterizedTest
        @MethodSource("userRoleProviderWhenFail")
        @DisplayName("실패 - 사용자 권한이 올바르지 않은 경우")
        void fail_signup_when_user_role_is_invalid(List<String> role) throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww")
                    .username("개발자")
                    .phoneNumber("010-0000-0000")
                    .roles(role)
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.message").value(HttpStatus.BAD_REQUEST.toString())
            );
        }

        @ParameterizedTest
        @MethodSource("userRoleProviderWhenSuccess")
        @DisplayName("성공 - 올바른 사용자 권한을 전달한 경우")
        void success_signup_when_user_role_is_invalid(List<String> role) throws Exception {
            //given
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qw!qwe")
                    .username("개발자")
                    .phoneNumber("010-0000-0000")
                    .roles(role)
                    .build();
            String content = objectMapper.writeValueAsString(signUpDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/signup")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isOk(),
                    jsonPath("$.status").value("SUCCESS")
            );
        }

        public Stream<Arguments> userRoleProviderWhenFail() {
            return Stream.of(
                    Arguments.arguments(List.of("ROLE_ANY"))
            );
        }

        public Stream<Arguments> userRoleProviderWhenSuccess() {
            return Stream.of(
                    Arguments.arguments(List.of(Role.ROLE_USER.toString())),
                    Arguments.arguments(List.of(Role.ROLE_PARTNER.toString())),
                    Arguments.arguments(List.of(Role.ROLE_VISITOR.toString()))
            );
        }
    }


}