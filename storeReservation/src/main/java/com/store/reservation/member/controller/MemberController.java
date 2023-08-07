package com.store.reservation.member.controller;


import com.store.reservation.member.domain.Member;
import com.store.reservation.member.dto.SignIn;
import com.store.reservation.member.dto.SignUp;
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

    /**
     * 회원 서비스 객체를 통해 회원 가입을 수행
     *
     * @param signUpRequest: 회원 가입 시 필요한 정보
     * @return 회원 가입 응답 dto
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUp.Request signUpRequest) {
        return ResponseEntity.ok(SignUp.Response.from(this.customerService.register(signUpRequest)));
    }

    /**
     * 회원 서비스 객체를 통해 회원 로그인을 수행
     *
     * @param signInRequest: 회원 로그인 시 필요한 정보
     * @return 회원 로그인이 완료된 회원의 토큰
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignIn.Request signInRequest) {
        Member authenticatedMember = this.customerService.authenticate(signInRequest);
        return ResponseEntity.ok(
                this.tokenProvider.generateToken(
                        authenticatedMember.getEmail(),
                        authenticatedMember.getRoles().stream()
                        .map(String::valueOf)
                        .collect(Collectors.toList()))
        );

    }

}
