package com.store.reservation.member.service;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.exception.MemberRuntimeException;
import com.store.reservation.member.repository.MemberInformationRepository;
import com.store.reservation.member.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.store.reservation.member.exception.MemberError.*;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberInformationRepository memberInformationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

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

    public MemberInformation authenticate(SignInDto.Request request) {
        MemberInformation memberInformation = memberInformationRepository.findByEmail(
                request.getEmail()).orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));
        if (!this.passwordEncoder.matches(request.getPassword(), memberInformation.getPassword())) {
            throw new MemberRuntimeException(PASSWORD_NOT_MATCH);
        }
        return memberInformation;
    }

    public SignInDto.Response signIn(SignInDto.Request signInRequest) {
        MemberInformation memberInformation = memberInformationRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));
        if (!this.passwordEncoder.matches(memberInformation.getPassword(), this.passwordEncoder.encode(signInRequest.getPassword()))) {
            throw new MemberRuntimeException(PASSWORD_NOT_MATCH);
        }
        String accessToken = this.tokenProvider.generateToken(memberInformation.getEmail(), memberInformation.getRoles());
        return SignInDto.Response.from(memberInformation.getId(), accessToken);
    }

    @Override
    public MemberInformation searchBy(long memberId) {
        return memberInformationRepository.findById(memberId)
                .orElseThrow(() -> new MemberRuntimeException(NO_SUCH_MEMBER));
    }

}
