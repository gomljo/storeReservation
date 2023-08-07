package com.store.reservation.member.security;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.model.SecurityUser;
import com.store.reservation.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 함수 설명:
     * 회원의 아이디를 이용하여 개인 회원 저장소에서 아이디와
     * 일치하는 개인 회원를 찾는 메서드
     *
     * @param email the email identifying the user whose data is required.
     * @return 전달받은 개인 회원의 아이디에 해당하는 개인 회원 객체
     * @throws UsernameNotFoundException : 전달 받은 회원 아이디에 해당하는 개인 회원이 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("이메일로 회원 조회 시작");
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        log.info("이메일로 회원 조회 정상 완료");
        return new SecurityUser(member);
    }
}
