package com.store.reservation.member.service;


import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.dto.SignInDto;
import com.store.reservation.member.dto.SignUpDto;

public interface MemberService {

    void register(SignUpDto user);
    MemberInformation authenticate(SignInDto.Request member);
    MemberInformation searchBy(long memberId);

}
