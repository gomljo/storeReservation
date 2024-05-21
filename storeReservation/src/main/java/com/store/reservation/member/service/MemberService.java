package com.store.reservation.member.service;

import com.store.reservation.auth.refreshToken.service.RefreshTokenService;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.exception.MemberRuntimeException;
import com.store.reservation.member.repository.MemberInformationRepository;
import com.store.reservation.member.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.store.reservation.member.exception.MemberError.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberInformationRepository memberInformationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final
    @Override
    @Transactional
    public void register(SignUpDto signUpDto) {

        if (memberInformationRepository.existsByEmail(signUpDto.getEmail())) {
            throw new MemberRuntimeException(ALREADY_JOINED_CUSTOMER);
        }

        String encodedPassword = this.passwordEncoder.encode(signUpDto.getPassword());
        memberInformationRepository.save(MemberInformation.builder()
                .email(signUpDto.getEmail())
                .password(encodedPassword)
                .phoneNumber(signUpDto.getPhoneNumber())
                .name(signUpDto.getUsername())
                .roles(signUpDto.getRoles())
                .build());
    }

    public SignInDto.Response signIn(SignInDto.Request signInRequest) {
        MemberInformation memberInformation = memberInformationRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));

        if (!this.passwordEncoder.matches(signInRequest.getPassword(), memberInformation.getPassword())) {
            throw new MemberRuntimeException(PASSWORD_NOT_MATCH);
        }

        String accessToken = this.tokenProvider.generateToken(memberInformation.getEmail(),
                memberInformation.getRoles(),
                signInRequest.getToday());
        String refreshToken = this.tokenProvider.generateToken(memberInformation.getEmail(),
                memberInformation.getRoles(),
                signInRequest.getToday());
        this.refreshTokenService.save(memberInformation.getEmail(), refreshToken, signInRequest.getToday());
        return SignInDto.Response.from(memberInformation.getId(), accessToken);
    }

    @Override
    public MemberInformation searchBy(long memberId) {
        return memberInformationRepository.findById(memberId)
                .orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));
    }

    @Override
    public void logout(String email) {

    }

}
