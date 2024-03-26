package com.store.reservation.member.memberInfo.interceptor;

import com.store.reservation.member.repository.MemberInformationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {
    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";
    private final MemberInformationRepository memberInformationRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청 전처리 로직 구현
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request
            , HttpServletResponse response
            , Object handler, ModelAndView modelAndView)
            throws Exception {
        // 요청 후처리 로직 구현
    }

    @Override
    public void afterCompletion(HttpServletRequest request
            , HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 예외 처리 로직 구현
    }
}
