package com.store.reservation.notification.token.service;

import com.store.reservation.notification.token.document.FCMToken;
import com.store.reservation.notification.token.document.Token;
import com.store.reservation.notification.token.exception.FCMTokenErrorCode;
import com.store.reservation.notification.token.exception.FCMTokenException;
import com.store.reservation.notification.token.repository.FCMTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.store.reservation.notification.token.exception.FCMTokenErrorCode.ALREADY_EXISTS;
import static com.store.reservation.notification.token.exception.FCMTokenErrorCode.NO_SUCH_TOKEN;

@Service
@RequiredArgsConstructor
public class FCMTokenService {

    private final FCMTokenRepository fcmTokenRepository;

    public void createNewToken(String email, String deviceUUID, String hash) {
        if (!fcmTokenRepository.existsById(email)) {
            throw new FCMTokenException(ALREADY_EXISTS);
        }
        fcmTokenRepository.save(FCMToken.builder()
                .email(email)
                .tokens(List.of(Token.builder()
                        .deviceUUID(deviceUUID)
                        .hash(hash)
                        .build()))
                .build());
    }

    public void addNewToken(String email, String deviceUUID, String hash){
        FCMToken fcmToken = fcmTokenRepository.findById(email)
                .orElseThrow(()->new FCMTokenException(NO_SUCH_TOKEN));
        fcmToken.addToken(Token.builder()
                        .deviceUUID(deviceUUID)
                        .hash(hash)
                .build());
        fcmTokenRepository.save(fcmToken);

    }

    public Token search(String email, String deviceUUID) {
        FCMToken fcmToken = fcmTokenRepository.findById(email)
                .orElseThrow(() -> new FCMTokenException(NO_SUCH_TOKEN));
        return fcmToken.getToken(deviceUUID);
    }
}
