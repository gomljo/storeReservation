package com.store.reservation.member.service;

import com.store.reservation.auth.refreshToken.exception.TokenErrorCode;
import com.store.reservation.auth.refreshToken.exception.TokenException;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.dto.ReissueTokenDto;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.dto.TokenDto;
import com.store.reservation.member.exception.MemberError;
import com.store.reservation.member.exception.MemberRuntimeException;
import com.store.reservation.member.repository.MemberInformationRepository;
import com.store.reservation.member.security.provider.JWTProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.store.reservation.auth.refreshToken.exception.TokenErrorCode.*;
import static com.store.reservation.member.exception.MemberError.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private JWTProvider jwtProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MemberInformationRepository memberInformationRepository;

    private MemberService memberService;

    @BeforeEach
    void setup() {
        this.memberService = new MemberService(memberInformationRepository,
                passwordEncoder,
                jwtProvider);
    }

    @Nested
    @DisplayName("회원 가입")
    class SignupTest {
        @Test
        @DisplayName("성공")
        void success_register() {
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345!Qw")
                    .username("개발자1")
                    .phoneNumber("010-0000-0000")
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();
            String encodedPassword = "encodedPassword";
            MemberInformation memberInformation = MemberInformation.builder()
                    .id(1L)
                    .name(signUpDto.getUsername())
                    .email(signUpDto.getEmail())
                    .password(encodedPassword)
                    .phoneNumber(signUpDto.getPhoneNumber())
                    .roles(signUpDto.getRoles())
                    .build();

            // given
            given(memberInformationRepository.existsByEmail(anyString()))
                    .willReturn(false);
            given(passwordEncoder.encode(anyString()))
                    .willReturn(encodedPassword);
            given(memberInformationRepository.save(any()))
                    .willReturn(memberInformation);

            // when
            ArgumentCaptor<MemberInformation> captor = ArgumentCaptor.forClass(MemberInformation.class);
            memberService.register(signUpDto);

            // then
            verify(memberInformationRepository).save(captor.capture());
            MemberInformation actualMemberInformation = captor.getValue();

            assertEquals(memberInformation.getEmail(), actualMemberInformation.getEmail());
            assertEquals(memberInformation.getPassword(), actualMemberInformation.getPassword());
            assertEquals(memberInformation.getRoles(), actualMemberInformation.getRoles());
            assertEquals(memberInformation.getPhoneNumber(), actualMemberInformation.getPhoneNumber());
        }

        @Test
        @DisplayName("실패 - 중복된 이메일로 가입하려는 경우")
        void fail_when_register_already_exist_email() {
            SignUpDto signUpDto = SignUpDto.builder()
                    .email("dev@gmail.com")
                    .password("12345!Qw")
                    .username("개발자1")
                    .phoneNumber("010-0000-0000")
                    .roles(List.of(Role.ROLE_USER.toString()))
                    .build();

            // given
            given(memberInformationRepository.existsByEmail(anyString()))
                    .willReturn(true);

            // when
            MemberRuntimeException memberRuntimeException = assertThrows(MemberRuntimeException.class, () -> memberService.register(signUpDto));

            // then

            assertEquals(memberRuntimeException.getDescription(), MemberError.ALREADY_JOINED_CUSTOMER.getDescription());
            assertEquals(memberRuntimeException.getErrorCode(), MemberError.ALREADY_JOINED_CUSTOMER.name());
        }
    }

    @Nested
    @DisplayName("로그인 기능 테스트")
    class LoginTest {
        @Test
        @DisplayName("성공 - 회원 가입한 상태이고 올바른 이메일과 패스워드를 입력했다.")
        void success() {
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("12345!Qw")
                    .today(today)
                    .build();
            MemberInformation memberInformation = MemberInformation.builder()
                    .email(signInDto.getEmail())
                    .password(signInDto.getPassword())
                    .build();
            String accessToken = "accessToken";
            String refreshToken = "refreshToken";
            given(memberInformationRepository.findByEmail(anyString())).willReturn(Optional.of(memberInformation));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(jwtProvider.generateAccessToken(anyString(), any())).willReturn(accessToken);
            given(jwtProvider.issueRefreshToken(anyString(), any())).willReturn(refreshToken);

            TokenDto tokenDto = memberService.signIn(signInDto);

            assertEquals(tokenDto.getAccessToken(), accessToken);
            assertEquals(tokenDto.getRefreshToken(), refreshToken);
        }

        @Test
        @DisplayName("실패 - 회원 가입한 적이 없는 경우")
        void fail_because_have_not_signup() {
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("12345!Qw")
                    .today(today)
                    .build();
            MemberRuntimeException expectedException = new MemberRuntimeException(NO_SUCH_MEMBER);
            given(memberInformationRepository.findByEmail(anyString())).willThrow(expectedException);

            MemberRuntimeException actualException = assertThrows(MemberRuntimeException.class, () -> memberService.signIn(signInDto));

            assertEquals(expectedException.getErrorCode(), actualException.getErrorCode());
            assertEquals(expectedException.getDescription(), actualException.getDescription());
        }

        @Test
        @DisplayName("실패 - 비밀번호를 잘못 입력한 경우")
        void fail_because_enter_wrong_password() {
            LocalDateTime today = LocalDateTime.now();
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("wrong_password")
                    .today(today)
                    .build();
            MemberInformation memberInformation = MemberInformation.builder()
                    .email(signInDto.getEmail())
                    .password(signInDto.getPassword())
                    .build();
            MemberRuntimeException expectedException = new MemberRuntimeException(PASSWORD_NOT_MATCH);
            given(memberInformationRepository.findByEmail(anyString())).willReturn(Optional.of(memberInformation));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
            MemberRuntimeException actualException = assertThrows(MemberRuntimeException.class, () -> memberService.signIn(signInDto));

            assertEquals(expectedException.getErrorCode(), actualException.getErrorCode());
            assertEquals(expectedException.getDescription(), actualException.getDescription());
        }

        @Test
        @DisplayName("실패 - 날짜 값이 비어있는 경우")
        void fail_because_enter_time_empty() {
            SignInDto signInDto = SignInDto.builder()
                    .email("dev@gmail.com")
                    .password("1234!Q1w")
                    .today(null)
                    .build();
            MemberInformation memberInformation = MemberInformation.builder()
                    .email(signInDto.getEmail())
                    .password(signInDto.getPassword())
                    .build();
            MemberRuntimeException expectedException = new MemberRuntimeException(TIME_EMPTY);
            given(memberInformationRepository.findByEmail(anyString())).willReturn(Optional.of(memberInformation));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            MemberRuntimeException actualException = assertThrows(MemberRuntimeException.class, () -> memberService.signIn(signInDto));

            assertEquals(expectedException.getErrorCode(), actualException.getErrorCode());
            assertEquals(expectedException.getDescription(), actualException.getDescription());
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class LogoutTest {
        @Test
        @DisplayName("성공")
        void success() {
            String email = "dev@gmail.com";

            doNothing().when(jwtProvider).deleteRefreshToken(anyString());
            memberService.logout(email);
            verify(jwtProvider, times(1)).deleteRefreshToken(anyString());
        }
    }

    @Nested
    @DisplayName("접근 토큰 재발급 테스트")
    class ReissueTest {

        @Test
        @DisplayName("성공 - 유효한 재발급 토큰과 요청자의 이메일 정보와 일치하고 요청 날짜 값이 null이 아닌 경우")
        void success(){
            // given
            String expectedAccessToken = "newAccessToken";
            String refreshToken = "validRefreshToken";
            String email = "dev@gmail.com";
            LocalDateTime today = LocalDateTime.now();
            ReissueTokenDto reissueTokenDto = new ReissueTokenDto(email, today);

            given(jwtProvider.reissue(anyString(), any(),any()))
                    .willReturn(expectedAccessToken);

            // when
            String actualToken = memberService.reissue(refreshToken, reissueTokenDto);

            // then
            assertEquals(expectedAccessToken, actualToken);
        }

        @Test
        @DisplayName("실패 - 만료된 재발급 토큰으로 재발급 요청하는 경우")
        void fail_when_expired_refresh_token(){
            // given
            String refreshToken = "expiredRefreshToken";
            String email = "dev@gmail.com";
            LocalDateTime today = LocalDateTime.now();
            ReissueTokenDto reissueTokenDto = new ReissueTokenDto(email, today);
            TokenException expectedTokenException = new TokenException(NO_SUCH_TOKEN);
            given(jwtProvider.reissue(anyString(), any(),any()))
                    .willThrow(expectedTokenException);

            // when
            TokenException actualTokenException = assertThrows(TokenException.class, ()->memberService.reissue(refreshToken, reissueTokenDto));

            // then
            assertEquals(expectedTokenException.getErrorCode(), actualTokenException.getErrorCode());
            assertEquals(expectedTokenException.getDescription(), actualTokenException.getDescription());
        }

        @Test
        @DisplayName("실패 - 다른 사용자의 refresh token으로 재발급하려는 경우")
        void fail_when_do_with_others_refresh_token(){
            // given
            String refreshToken = "expiredRefreshToken";
            String email = "dev@gmail.com";
            LocalDateTime today = LocalDateTime.now();
            ReissueTokenDto reissueTokenDto = new ReissueTokenDto(email, today);
            TokenException expectedTokenException = new TokenException(INVALID_ACCESS);
            given(jwtProvider.reissue(anyString(), any(),any()))
                    .willThrow(expectedTokenException);

            // when
            TokenException actualTokenException = assertThrows(TokenException.class,
                    ()->memberService.reissue(refreshToken, reissueTokenDto));

            // then
            assertEquals(expectedTokenException.getErrorCode(), actualTokenException.getErrorCode());
            assertEquals(expectedTokenException.getDescription(), actualTokenException.getDescription());
        }

        @Test
        @DisplayName("실패 - 재발급 토큰 값이 없는 경우")
        void fail_when_empty_refresh_token(){
            // given
            String refreshToken = "";
            String email = "dev@gmail.com";
            LocalDateTime today = LocalDateTime.now();
            ReissueTokenDto reissueTokenDto = new ReissueTokenDto(email, today);
            TokenException expectedTokenException = new TokenException(EMPTY_TOKEN);

            // when
            TokenException actualTokenException = assertThrows(TokenException.class, ()->memberService.reissue(refreshToken, reissueTokenDto));

            // then
            assertEquals(expectedTokenException.getErrorCode(), actualTokenException.getErrorCode());
            assertEquals(expectedTokenException.getDescription(), actualTokenException.getDescription());
        }

        @Test
        @DisplayName("실패 - 요청 날짜가 null인 경우")
        void fail_when_request_date_is_null(){
            // given
            String refreshToken = "nonExpiredRefreshToken";
            String email = "dev@gmail.com";
            LocalDateTime today = null;
            ReissueTokenDto reissueTokenDto = new ReissueTokenDto(email, today);
            MemberRuntimeException expectedException = new MemberRuntimeException(TIME_EMPTY);

            // when
            MemberRuntimeException actualException = assertThrows(MemberRuntimeException.class,
                    ()->memberService.reissue(refreshToken, reissueTokenDto));

            // then
            assertEquals(expectedException.getErrorCode(), actualException.getErrorCode());
            assertEquals(expectedException.getDescription(), actualException.getDescription());
        }

    }
}