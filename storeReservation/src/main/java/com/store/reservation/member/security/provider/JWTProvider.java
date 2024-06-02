package com.store.reservation.member.security.provider;


import com.store.reservation.auth.refreshToken.exception.TokenErrorCode;
import com.store.reservation.auth.refreshToken.exception.TokenException;
import com.store.reservation.auth.refreshToken.service.RefreshTokenService;
import com.store.reservation.common.constants.TokenExpirationConstant;
import com.store.reservation.member.security.jwt.Jwt;
import com.store.reservation.member.security.userDetailService.CustomUserDetailService;
import com.store.reservation.member.security.userDetails.SecurityUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;

import static com.store.reservation.auth.refreshToken.exception.TokenErrorCode.*;

@Component
@Getter
@RequiredArgsConstructor
public class JWTProvider {
    private static final String TOKEN_PREFIX = "Bearer";
    private final CustomUserDetailService customUserDetailService;
    private final RefreshTokenService refreshTokenService;
    private final Jwt jwt;
    private final TokenExpirationConstant tokenExpirationConstant;

    public String generateAccessToken(String email, Date today) {
        SecurityUser securityUser = customUserDetailService.loadUserByUsername(email);
        return this.jwt.generate(securityUser, today, tokenExpirationConstant.getAccessTokenExpiredDate(today));
    }

    public String issueRefreshToken(String email, Date today) {
        SecurityUser securityUser = customUserDetailService.loadUserByUsername(email);
        String refreshToken = this.jwt.generate(securityUser, today, tokenExpirationConstant.getRefreshTokenExpiredDate(today));
        this.refreshTokenService.save(email, refreshToken, tokenExpirationConstant.getRefreshTokenExpiredMinute());
        return refreshToken;
    }

    public String reissue(String email, String refreshToken, Date today) {
        if(!this.refreshTokenService.isSameToken(email, refreshToken)){
            throw new TokenException(INVALID_ACCESS);
        }
        return generateAccessToken(email, today);
    }


    public Authentication getAuthentication(String headerValue) {
        if (ObjectUtils.isEmpty(headerValue)) {
            throw new TokenException(EMPTY_TOKEN);
        }
        String token = this.eliminatePrefix(headerValue);
        String userName = this.jwt.getUserName(token);
        UserDetails userDetails = this.customUserDetailService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    public void validateToken(String headerValue, Date current) {
        if (!StringUtils.hasText(headerValue)) {
            throw new TokenException(EMPTY_TOKEN);
        }
        String token = this.eliminatePrefix(headerValue);
        Date tokenExpiration = this.jwt.isTokenExpired(token);
        if (tokenExpiration.before(current)) {
            throw new TokenException(TOKEN_EXPIRED);
        }
    }

    private String eliminatePrefix(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new TokenException(INVALID_PREFIX);
        }
        return token.substring(TOKEN_PREFIX.length());
    }

    public void deleteRefreshToken(String email) {
        if(!this.refreshTokenService.isRefreshTokenExpired(email)){
            this.refreshTokenService.delete(email);
        }
    }
}
