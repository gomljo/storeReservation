package com.store.reservation.annotation;


import com.store.reservation.member.security.filter.JwtAuthenticationFilter;
import com.store.reservation.member.security.provider.JWTProvider;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@Import({JWTProvider.class, JwtAuthenticationFilter.class})
public @interface UserTest {
}
