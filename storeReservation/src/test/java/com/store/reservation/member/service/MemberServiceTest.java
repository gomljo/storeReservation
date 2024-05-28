package com.store.reservation.member.service;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.dto.SignUpDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

}