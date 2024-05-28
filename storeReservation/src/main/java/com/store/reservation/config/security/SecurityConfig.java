package com.store.reservation.config.security;


import com.store.reservation.member.security.filter.JwtAuthenticationFilter;
import com.store.reservation.member.security.filter.JwtExceptionFilter;
import com.store.reservation.member.security.provider.JWTProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    public SecurityConfig(JWTProvider jwtProvider) {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProvider);
        this.jwtExceptionFilter = new JwtExceptionFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(
                        this.jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(this.jwtExceptionFilter, JwtAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers(
                        "/",
                        "/images/**",
                        "/js/**",
                        "/css/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/**",
                        "/v3/**",
                        "/**/member/signup",
                        "/**/auth/signin",
                        "/store/search"
                );
    }
}
