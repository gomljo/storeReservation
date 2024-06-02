package com.store.reservation.member.controller;

import com.store.reservation.apiResponse.ApiResponse;
import com.store.reservation.member.dto.ReissueTokenDto;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;
import com.store.reservation.member.dto.TokenDto;
import com.store.reservation.member.security.userDetails.SecurityUser;
import com.store.reservation.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "member-api", description = "회원 API")
public class MemberController {

    private final MemberService customerService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입 하기",
            description = "필요한 회원 정보(이름, 이메일(아이디), 패스워드, 핸드폰 번호, 권한)를 통해 회원 가입",
            tags = "member-api")
    public ApiResponse<?> signUp(@RequestBody @Valid SignUpDto signUpRequest) {
        this.customerService.register(signUpRequest);
        return ApiResponse.success();
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 하기",
            description = "이메일(아이디), 패스워드 정보를 통해 로그인",
            tags = "member-api")
    public ApiResponse<TokenDto> signIn(@RequestBody @Valid SignInDto signInRequest) {
        return ApiResponse.success(this.customerService.signIn(signInRequest));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 하기",
            description = "사용자의 토큰 정보를 이용하여 로그아웃",
            tags = "member-api")
    public ApiResponse<?> logout(@AuthenticationPrincipal SecurityUser securityUser) {
        this.customerService.logout(securityUser.getUsername());
        return ApiResponse.success();
    }

    @PostMapping("/reissue")
    @Operation(summary = "접근 토큰 재발급하기",
            description = "접근 토큰과 재발급 토큰으로 만료된 접근 토큰을 갱신",
            tags = "member-api")
    public ApiResponse<String> reissue(@RequestHeader("RefreshToken") String refreshToken,
                                       @RequestBody ReissueTokenDto reissueTokenDto) {
        return ApiResponse.success(this.customerService.reissue(refreshToken, reissueTokenDto));
    }

}
