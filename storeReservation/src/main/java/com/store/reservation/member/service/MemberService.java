package com.store.reservation.member.service;

import com.store.reservation.member.memberInfo.domain.MemberInformation;
import com.store.reservation.member.memberInfo.dto.SignInDto;
import com.store.reservation.member.memberInfo.dto.SignUpDto;

public interface MemberService {

    void register(SignUpDto user);
    MemberInformation authenticate(SignInDto.Request member);
    MemberInformation searchBy(long memberId);

}
