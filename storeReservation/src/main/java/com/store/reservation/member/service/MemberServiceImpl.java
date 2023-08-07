package com.store.reservation.member.service;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.dto.SignIn;
import com.store.reservation.member.dto.SignUp;
import com.store.reservation.member.exception.MemberException;
import com.store.reservation.member.model.SecurityUser;
import com.store.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.store.reservation.member.exception.MemberError.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 함수 설명:
     * 개인 회원 가입 요청 정보를 전달받아 개인 회원 정보를 저장소에 등록하는 함수
     *
     * @param user: 개인 회원 가입 요청 정보를 담은 개인 회원 객체
     * @return 개인 회원 가입 요청 정보를 이용하여 만들어진 개인 회원 객체
     */
    @Override
    @Transactional
    public Member register(SignUp.Request user) {

        log.info("[CustomerService]: 회원 등록 시작");
        if (memberRepository.existsByEmail(user.getId())) {
            throw new MemberException(ALREADY_JOINED_CUSTOMER);
        }

        String encodedPassword = this.passwordEncoder.encode(user.getPassword());

        log.info("[CustomerService]: 회원 등록 정상 완료");
        return memberRepository.save(Member.builder()
                .email(user.getId())
                .password(encodedPassword)
                .phoneNumber(user.getPhoneNumber())
                .name(user.getUsername())
                .roles(user.getRoles())
                .build());
    }


    /**
     * 함수 설명
     *
     * @param request: 로그인 시 필요한 정보를 지닌 로그인 객체
     * @return 로그인에 성공한 회원 객체
     */
    public Member authenticate(SignIn.Request request) {
        Member member = memberRepository.findByEmail(
                request.getId()).orElseThrow(() -> new MemberException(NO_SUCH_MEMBER));
        if (!this.passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(PASSWORD_NOT_MATCH);
        }
        return member;
    }




    @Override
    public Member searchBy(long memberId) {
        log.info("");
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(NO_SUCH_MEMBER));
    }

}
