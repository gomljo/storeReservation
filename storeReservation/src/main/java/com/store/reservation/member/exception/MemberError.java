package com.store.reservation.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberError {

    ALREADY_JOINED_CUSTOMER("이미 가입된 회원입니다."),
    NO_SUCH_MEMBER("회원이 존재하지 않습니다"),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다");

    private final String description;
}
