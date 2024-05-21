package com.store.reservation.member.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AuthenticationFilter {

    private static final String ACCESS_TOKEN_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "RefreshToken";
    private final TokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = this.resolveTokenFromRequest(request, ACCESS_TOKEN_HEADER);
        String refreshToken = this.resolveTokenFromRequest(request, REFRESH_TOKEN_HEADER);
        System.out.printf("%s %s\n", "at header: ", request.getHeader(ACCESS_TOKEN_HEADER));
        System.out.printf("%s %s\n", "rt header: ", request.getHeader(REFRESH_TOKEN_HEADER));
        System.out.printf("%s %s\n", "at: ", accessToken);
        System.out.printf("%s %s\n", "rt: ", refreshToken);

        if(StringUtils.hasText(accessToken)){
            String wantToValidate = accessToken;
            if(StringUtils.hasText(refreshToken)){
                wantToValidate = refreshToken;
            }
            this.tokenProvider.validateToken(wantToValidate);

            Authentication auth = this.tokenProvider.getAuthentication(wantToValidate);
            SecurityContextHolder.getContext().setAuthentication(auth);

        }

        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromRequest(HttpServletRequest request, String headerName) {
        return request.getHeader(headerName);
    }
}