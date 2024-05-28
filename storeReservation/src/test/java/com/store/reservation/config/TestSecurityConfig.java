package com.store.reservation.config;

import com.store.reservation.config.security.SecurityConfig;
import com.store.reservation.member.security.filter.JwtAuthenticationFilter;
import com.store.reservation.member.security.provider.JWTProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean
    private JWTProvider jwtProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    @Bean
    public SecurityConfig securityConfig() {
        return new SecurityConfig(jwtProvider);
    }

}
