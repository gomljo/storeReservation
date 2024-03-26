package com.store.reservation.member.controller;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.security.TokenProvider;
import com.store.reservation.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService customerService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public void signUp(@RequestBody SignUpDto signUpRequest) {
        this.customerService.register(signUpRequest);

    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDto.Request signInRequest) {
        MemberInformation authenticatedMemberInformation = this.customerService.authenticate(signInRequest);
        return ResponseEntity.ok(
                this.tokenProvider.generateToken(
                        authenticatedMemberInformation.getEmail(),
                        authenticatedMemberInformation.getRoles().stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList()))
        );

    }

}
