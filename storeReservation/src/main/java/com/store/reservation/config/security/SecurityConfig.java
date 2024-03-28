package com.store.reservation.config.security;



import com.store.reservation.member.security.JwtAuthenticationFilter;
import com.store.reservation.member.security.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityConfig(TokenProvider tokenProvider){
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter(tokenProvider);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/store/search/customer/**")
                .permitAll()
                .and()
                .addFilterBefore(
                        this.jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );
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
