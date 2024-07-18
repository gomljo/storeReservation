package com.store.reservation.annotation;

import com.store.reservation.member.domain.MemberInformation;
import com.store.reservation.member.domain.type.Role;
import com.store.reservation.member.security.userDetails.SecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomer> {
    @Override
    public SecurityContext createSecurityContext(WithCustomer withCustomer) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        MemberInformation customer = MemberInformation.builder()
                .name("홍길동")
                .email(withCustomer.email())
                .password("11111")
                .phoneNumber("010-111-111")
                .roles(List.of(Role.ROLE_PARTNER.toString()))
                .build();
        UserDetails securityUser = new SecurityUser(customer);

        Authentication auth = new UsernamePasswordAuthenticationToken(securityUser, "", securityUser.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
