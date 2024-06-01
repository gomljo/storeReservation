package com.store.reservation.member.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.reservation.annotation.WithCustomer;
import com.store.reservation.config.TestSecurityConfig;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.dto.SignInDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.store.reservation.member.exception.MemberError.*;
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
    @DisplayName("회원가입 API 테스트")
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
        void success_signup_when_user_role_is_valid(List<String> role) throws Exception {
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

    @Nested
    @DisplayName("로그인 API 테스트")
    class SignInRequest {
        @Test
        @DisplayName("성공")
        void success() throws Exception {

            //given
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww!sadf")
                    .today(today)
                    .build();
            String content = objectMapper.writeValueAsString(signInDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/login")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isOk(),
                    jsonPath("$.status").value("SUCCESS")
            );
        }

        @Test
        @DisplayName("실패 - 회원 가입한 적이 없는 경우")
        void fail_when_have_not_signup_return_bad_request() throws Exception {
            //given
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww!sadf")
                    .today(today)
                    .build();
            MemberRuntimeException expectedException = new MemberRuntimeException(NO_SUCH_MEMBER);
            doThrow(expectedException).when(memberService).signIn(any());
            String content = objectMapper.writeValueAsString(signInDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/login")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.data").value(expectedException.getErrorCode()),
                    jsonPath("$.message").value(expectedException.getDescription())
            );
        }

        @Test
        @DisplayName("실패 - 비밀번호를 잘못 입력한 경우")
        void fail_when_enter_wrong_password_then_return_bad_request() throws Exception {
            //given
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("12345Qww!sadf")
                    .today(today)
                    .build();
            MemberRuntimeException expectedException = new MemberRuntimeException(PASSWORD_NOT_MATCH);
            doThrow(expectedException).when(memberService).signIn(any());
            String content = objectMapper.writeValueAsString(signInDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/login")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.data").value(expectedException.getErrorCode()),
                    jsonPath("$.message").value(expectedException.getDescription())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"@gmail.com", "dev@gmail", "!@#$!$#!@@gmail.com"})
        @DisplayName("실패 - 이메일 형식이 올바르지 않은 경우")
        void fail_because_invalid_email_form_then_return_bad_request(String email) throws Exception {
            //given
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email(email)
                    .password("12345Qww!sadf")
                    .today(today)
                    .build();
            String content = objectMapper.writeValueAsString(signInDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/login")
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest(),
                    jsonPath("$.status").value("ERROR"),
                    jsonPath("$.data").value("이메일 형식에 맞춰서 요청해주세요"),
                    jsonPath("$.message").value(HttpStatus.BAD_REQUEST.toString())
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {"12344554", "123445Q", "!@1234W", "213141w"})
        @DisplayName("실패 - 패스워드 형식이 올바르지 않은 경우")
        void fail_because_invalid_password_then_return_bad_request(String password) throws Exception {
            //given
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password(password)
                    .today(today)
                    .build();
            String content = objectMapper.writeValueAsString(signInDto);
            //when
            ResultActions perform = mockMvc.perform(post("/member/login")
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

        @Test
        public void fail_when_Null_Date_then_return_bad_request() throws Exception {
            //given
            SignInDto signInDto = SignInDto.builder()
                    .email("valid.email@example.com")
                    .password("ValidPassword123!")
                    .today(null)
                    .build();
            MemberRuntimeException memberRuntimeException = new MemberRuntimeException(TIME_EMPTY);
            doThrow(memberRuntimeException).when(memberService).signIn(any());
            // when
            ResultActions actions = mockMvc.perform(post("/member/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(signInDto)));
            // then
            actions.andExpect(status().isBadRequest());
        }
    }
    @Nested
    @DisplayName("로그아웃 API 테스트")
    class LogoutTest{
        @Test
        @DisplayName("성공")
        @WithCustomer(email = "dev@gmail.com")
        void success() throws Exception {
            //given

            //when
            ResultActions perform = mockMvc.perform(post("/member/logout")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isOk()
            );
        }

        @Test
        @DisplayName("실패 - 토큰이 없는 경우")
        void fail_when_request_with_empty_token_return_bad_request() throws Exception {
            //given

            //when
            ResultActions perform = mockMvc.perform(post("/member/logout")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON));

            //then
            perform.andDo(print()).andExpectAll(
                    status().isBadRequest()
            );
        }

    }


}