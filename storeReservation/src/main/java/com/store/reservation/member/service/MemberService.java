package com.store.reservation.member.service;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.dto.SignIn;
import com.store.reservation.member.dto.SignUp;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService {

    Member register(SignUp.Request user);
    Member authenticate(SignIn.Request member);
    Member searchBy(long memberId);

}
