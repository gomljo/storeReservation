package com.store.reservation.member.service;

import com.store.reservation.auth.refreshToken.exception.TokenErrorCode;
import com.store.reservation.auth.refreshToken.exception.TokenException;
import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.dto.ReissueTokenDto;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.dto.TokenDto;
import com.store.reservation.member.exception.MemberRuntimeException;
import com.store.reservation.member.repository.MemberInformationRepository;
import com.store.reservation.member.security.provider.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.store.reservation.member.exception.MemberError.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberInformationRepository memberInformationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTProvider jwtProvider;


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

    public TokenDto signIn(SignInDto signInRequest) {
        MemberInformation memberInformation = memberInformationRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));

        if (!this.passwordEncoder.matches(signInRequest.getPassword(), memberInformation.getPassword())) {
            throw new MemberRuntimeException(PASSWORD_NOT_MATCH);
        }

        if (Objects.isNull(signInRequest.getDate())) {
            throw new MemberRuntimeException(TIME_EMPTY);
        }

        String accessToken = this.jwtProvider.generateAccessToken(memberInformation.getEmail(), signInRequest.getDate());
        String refreshToken = this.jwtProvider.issueRefreshToken(memberInformation.getEmail(), signInRequest.getDate());

        return new TokenDto(accessToken, refreshToken);
    }

    public MemberInformation searchBy(long memberId) {
        return memberInformationRepository.findById(memberId)
                .orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));
    }

    public void logout(String email) {
        this.jwtProvider.deleteRefreshToken(email);
    }

    public String reissue(String refreshToken, ReissueTokenDto reissueTokenDto) {
        if (refreshToken.isEmpty()) {
            throw new TokenException(TokenErrorCode.EMPTY_TOKEN);
        }
        if (Objects.isNull(reissueTokenDto.getDate())) {
            throw new MemberRuntimeException(TIME_EMPTY);
        }
        return this.jwtProvider.reissue(reissueTokenDto.getEmail(), refreshToken, reissueTokenDto.getDate());
    }
}
