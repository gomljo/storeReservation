package com.store.reservation.member.repository;

import com.store.reservation.member.domain.Member;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.exception.MemberError;
import com.store.reservation.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(properties = {"spring.config.location=classpath:application-test.yml"})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("개인 회원 저장 성공")
    void success_save(){

        // given
        Member member = Member.builder()
                .email("1111@111.com")
                .name("홍길동")
                .phoneNumber("010-1111-1111")
                .password("1111")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        // when
        Member save = memberRepository.save(member);
        // then
        LocalDateTime createdAt = save.getCreateAt();

        assertEquals(save.getMemberId(), member.getMemberId());
        assertEquals(save.getEmail(), member.getEmail());
        assertEquals(save.getPassword(), member.getPassword());
        assertEquals(save.getPhoneNumber(), member.getPhoneNumber());
        assertEquals(save.getRoles(), member.getRoles());
        assertEquals(createdAt.getDayOfMonth(), LocalDate.now().getDayOfMonth());
    }

    @Test
    @DisplayName("개인 회원 이메일로 조회 성공")
    void success_findByEmail(){

        // given
        Member member = Member.builder()
                .email("1111@111.com")
                .name("홍길동")
                .phoneNumber("010-1111-1111")
                .password("1111")
                .roles(List.of(Role.ROLE_USER.toString()))
                .build();

        // when
        Member save = memberRepository.save(member);
        Member byEmail = memberRepository.findByEmail(save.getEmail()).orElseThrow(()->new MemberException(MemberError.NO_SUCH_MEMBER));
        // then
        assertNotNull(byEmail);
        assertEquals(byEmail.getMemberId(), save.getMemberId());
    }

}