package com.store.reservation.annotation;


import com.store.reservation.member.security.CustomUserDetailService;
import com.store.reservation.member.security.JwtAuthenticationFilter;
import com.store.reservation.member.security.TokenProvider;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@Import({TokenProvider.class, JwtAuthenticationFilter.class})
public @interface UserTest {
}
