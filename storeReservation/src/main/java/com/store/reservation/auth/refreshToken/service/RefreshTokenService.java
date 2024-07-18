package com.store.reservation.auth.refreshToken.service;

import com.store.reservation.auth.refreshToken.domain.RefreshToken;
import com.store.reservation.auth.refreshToken.exception.TokenException;
import com.store.reservation.auth.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Optional;

import static com.store.reservation.auth.refreshToken.exception.TokenErrorCode.NO_SUCH_TOKEN;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(String email, String refreshToken, long expiration) {
        refreshTokenRepository.save(RefreshToken.builder()
                .email(email)
                .refreshToken(refreshToken)
                .expiration(expiration)
                .build());
    }

    public boolean isRefreshTokenExpired(String email) {
        return !refreshTokenRepository.existsById(email);
    }

    public void delete(String email) {
        refreshTokenRepository.deleteById(email);
    }
    public boolean isSameToken(String email, String requestedRefreshToken){
        RefreshToken refreshToken = refreshTokenRepository.findById(email).orElseThrow(()->new TokenException(NO_SUCH_TOKEN));
        return refreshToken.isSameToken(requestedRefreshToken);
    }
}
