package com.store.reservation.member.service;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.dto.TokenDto;
import com.store.reservation.member.exception.MemberRuntimeException;
import com.store.reservation.member.repository.MemberInformationRepository;
import com.store.reservation.member.security.provider.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    public String reissue(String refreshToken, Date today) {
        Authentication authentication = this.jwtProvider.getAuthentication(refreshToken);
        if (this.jwtProvider.isExpired(authentication.getName())) {
            throw new MemberRuntimeException(TOKEN_EXPIRED);
        }
        return this.jwtProvider.generateAccessToken(authentication.getName(), today);
    }
}
