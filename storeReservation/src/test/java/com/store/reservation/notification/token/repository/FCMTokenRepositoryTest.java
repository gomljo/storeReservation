package com.store.reservation.notification.token.repository;

import com.store.reservation.notification.token.document.FCMToken;
import com.store.reservation.notification.token.document.Token;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@DataMongoTest
@ActiveProfiles(profiles = {"dev"})
class FCMTokenRepositoryTest {
    @Autowired
    private FCMTokenRepository fcmTokenRepository;

    @AfterEach
    void setup() {
        fcmTokenRepository.deleteAll();
    }


    @Test
    @DisplayName("토큰 저장 성공")
    public void success_save_token() {
        String deviceUUID1 = UUID.randomUUID().toString();
        FCMToken fcmToken = FCMToken.builder()
                .email("1234@gmail.com")
                .tokens(List.of(Token.builder()
                        .deviceUUID(deviceUUID1)
                        .hash("hash1")
                        .build()))
                .build();
        fcmTokenRepository.save(fcmToken);
        Optional<FCMToken> byEmail = fcmTokenRepository.findByEmail("1234@gmail.com");
        assertTrue(byEmail.isPresent());
        FCMToken expected = byEmail.get();
        assertEquals(expected.getEmail(), "1234@gmail.com");
        assertEquals(expected.getTokens().size(), fcmToken.getTokens().size());
        for (int i = 0; i < expected.getTokens().size(); i++) {
            assertEquals(expected.getTokens().get(i).getDeviceUUID(), fcmToken.getTokens().get(i).getDeviceUUID());
            assertEquals(expected.getTokens().get(i).getHash(), fcmToken.getTokens().get(i).getHash());
        }
    }

    @Test
    @DisplayName("토큰 추가 성공")
    public void success_add_token(){
        String deviceUUID1 = UUID.randomUUID().toString();
        String deviceUUID2 = UUID.randomUUID().toString();
        FCMToken fcmToken = FCMToken.builder()
                .email("1234@gmail.com")
                .tokens(List.of(Token.builder()
                        .deviceUUID(deviceUUID1)
                        .hash("hash1")
                        .build()))
                .build();
        fcmTokenRepository.save(fcmToken);
        fcmToken.addToken(Token.builder()
                .deviceUUID(deviceUUID2)
                .hash("hash2")
                .build());
        fcmTokenRepository.save(fcmToken);
        Optional<FCMToken> byEmail = fcmTokenRepository.findByEmail("1234@gmail.com");
        assertTrue(byEmail.isPresent());
        FCMToken expected = byEmail.get();
        assertEquals(expected.getEmail(), "1234@gmail.com");
        assertEquals(expected.getTokens().size(), fcmToken.getTokens().size());
        for (int i = 0; i < expected.getTokens().size(); i++) {
            assertEquals(expected.getTokens().get(i).getDeviceUUID(), fcmToken.getTokens().get(i).getDeviceUUID());
            assertEquals(expected.getTokens().get(i).getHash(), fcmToken.getTokens().get(i).getHash());
        }
    }
}