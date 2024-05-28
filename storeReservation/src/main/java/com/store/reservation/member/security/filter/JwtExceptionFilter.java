package com.store.reservation.member.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.reservation.apiResponse.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            doFilter(request, response, filterChain);
        } catch (RuntimeException runtimeException) {
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), ApiResponse.error(HttpStatus.BAD_REQUEST.toString()));
        }
    }
}
