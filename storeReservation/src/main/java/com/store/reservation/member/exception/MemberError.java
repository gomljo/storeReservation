package com.store.reservation.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberError {

    ALREADY_JOINED_CUSTOMER("이미 가입된 회원입니다."),
    NO_SUCH_MEMBER("회원이 존재하지 않습니다"),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다"),
    ACCESS_DENIED("올바른 데이터 접근이 아닙니다."),
    TOKEN_EXPIRED("토큰이 만료되었습니다.");

    private final String description;
}
