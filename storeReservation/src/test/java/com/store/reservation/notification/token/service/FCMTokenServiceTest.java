package com.store.reservation.notification.token.service;

import com.store.reservation.notification.dto.FCMTokenDto;
import com.store.reservation.notification.token.document.FCMToken;
import com.store.reservation.notification.token.document.Token;
import com.store.reservation.notification.token.exception.FCMTokenException;
import com.store.reservation.notification.token.repository.FCMTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.store.reservation.notification.token.exception.FCMTokenErrorCode.ALREADY_EXISTS;
import static com.store.reservation.notification.token.exception.FCMTokenErrorCode.NO_SUCH_TOKEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FCMTokenServiceTest {

    @Mock
    private FCMTokenRepository fcmTokenRepository;

    @InjectMocks
    private FCMTokenService fcmTokenService;


    @Nested
    @DisplayName("토큰 저장 테스트")
    class TokenSaveTest {
        @Test
        @DisplayName("FCM 토큰 저장 성공")
        public void success_save_token() {
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String hash = "hash1";
            FCMToken fcmToken = FCMToken.builder()
                    .email(email)
                    .tokens(List.of(Token.builder()
                            .deviceUUID(uuid)
                            .hash(hash)
                            .build()))
                    .build();
            given(fcmTokenRepository.existsByEmail(email))
                    .willReturn(false);
            given(fcmTokenRepository.save(any()))
                    .willReturn(fcmToken);

            fcmTokenService.createNewToken(FCMTokenDto.builder()
                    .email(email)
                    .deviceUUID(uuid)
                    .hash(hash)
                    .build());
            ArgumentCaptor<FCMToken> fcmTokenArgumentCaptor = ArgumentCaptor.forClass(FCMToken.class);
            verify(fcmTokenRepository, times(1)).existsByEmail(email);
            verify(fcmTokenRepository, times(1)).save(fcmTokenArgumentCaptor.capture());
            FCMToken expected = fcmTokenArgumentCaptor.getValue();
            assertEquals(expected.getEmail(), fcmToken.getEmail());
        }

        @Test
        @DisplayName("FCM 토큰 저장 실패 - 토큰이 이미 존재함")
        public void fail_save_token_because_already_exists() {
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String hash = "hash1";
            given(fcmTokenRepository.existsByEmail(email))
                    .willReturn(true);
            FCMTokenException fcmTokenException = assertThrows(FCMTokenException.class, () -> fcmTokenService.createNewToken(FCMTokenDto.builder()
                    .email(email)
                    .deviceUUID(uuid)
                    .hash(hash)
                    .build()));
            verify(fcmTokenRepository, times(1)).existsByEmail(email);
            assertEquals(fcmTokenException.getDescription(), ALREADY_EXISTS.getDescription());
        }
    }

    @Nested
    @DisplayName("토큰 추가 테스트")
    class TokenAdditionTest {
        @Test
        @DisplayName("사용자의 새로운 기기의 FCM 토큰 추가 성공")
        public void success_add_new_token() {
            // initialize
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String uuid2 = UUID.randomUUID().toString();
            String hash = "hash1";
            String hash2 = "hash2";
            FCMToken fcmToken = FCMToken.builder()
                    .email(email)
                    .tokens(List.of(Token.builder()
                            .deviceUUID(uuid)
                            .hash(hash)
                            .build()))
                    .build();
            Token newToken = Token.builder()
                    .deviceUUID(uuid2)
                    .hash(hash2)
                    .build();

            // given
            given(fcmTokenRepository.findById(email))
                    .willReturn(Optional.of(fcmToken));
            fcmToken.addToken(newToken);
            given(fcmTokenRepository.save(any()))
                    .willReturn(fcmToken);
            // when
            fcmTokenService.addNewToken(email, uuid, hash);

            // then
            ArgumentCaptor<FCMToken> fcmTokenArgumentCaptor = ArgumentCaptor.forClass(FCMToken.class);
            verify(fcmTokenRepository, times(1)).findById(email);
            verify(fcmTokenRepository, times(1)).save(fcmTokenArgumentCaptor.capture());

            FCMToken expected = fcmTokenArgumentCaptor.getValue();

            assertEquals(expected.getEmail(), fcmToken.getEmail());
            assertEquals(expected.getTokens().size(), fcmToken.getTokens().size());

            for (int i = 0; i < expected.getTokens().size(); i++) {
                Token expectedToken = expected.getTokens().get(i);
                Token actualToken = fcmToken.getTokens().get(i);
                assertEquals(expectedToken.getDeviceUUID(), actualToken.getDeviceUUID());
                assertEquals(expectedToken.getHash(), actualToken.getHash());
            }
        }

        @Test
        @DisplayName("사용자의 새로운 기기에 대한 FCM 토큰 추가 실패 - 사용자의 이메일로 토큰을 찾을 수 없는 경우")
        public void fail_add_new_token_because_invalid_email() {
            // initialize
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String hash = "hash1";

            // given
            given(fcmTokenRepository.findById(email))
                    .willThrow(new FCMTokenException(NO_SUCH_TOKEN));

            // when
            FCMTokenException fcmTokenException = assertThrows(FCMTokenException.class, () -> fcmTokenService.addNewToken(email, uuid, hash));

            // then
            verify(fcmTokenRepository, times(1)).findById(email);
            assertEquals(fcmTokenException.getDescription(), NO_SUCH_TOKEN.getDescription());
        }

        @Test
        @DisplayName("사용자의 새로운 기기에 대한 FCM 토큰 추가 실패 - 새로운 기기인줄 알았지만 이미 등록된 토큰인 경우")
        public void fail_add_new_token_because_already_exists() {
            // initialize
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String hash = "hash1";
            FCMToken fcmToken = FCMToken.builder()
                    .email(email)
                    .tokens(List.of(Token.builder()
                            .deviceUUID(uuid)
                            .hash(hash)
                            .build()))
                    .build();

            int numberOfTokensBeforeAddition = fcmToken.getTokens().size();

            Token newToken = Token.builder()
                    .deviceUUID(uuid)
                    .hash(hash)
                    .build();

            // given
            given(fcmTokenRepository.findById(email))
                    .willReturn(Optional.of(fcmToken));
            fcmToken.addToken(newToken);
            given(fcmTokenRepository.save(any()))
                    .willReturn(fcmToken);

            // when
            fcmTokenService.addNewToken(email, uuid, hash);

            // then
            ArgumentCaptor<FCMToken> argumentCaptor = ArgumentCaptor.forClass(FCMToken.class);
            verify(fcmTokenRepository, times(1)).findById(email);
            verify(fcmTokenRepository, times(1)).save(argumentCaptor.capture());

            FCMToken afterAddition = argumentCaptor.getValue();

            assertEquals(fcmToken.getEmail(), afterAddition.getEmail());
            assertEquals(numberOfTokensBeforeAddition, afterAddition.getTokens().size());

            List<Token> actualTokens = fcmToken.getTokens();
            for (Token actual : afterAddition.getTokens()) {
                assertTrue(actualTokens.contains(actual));
            }
        }
    }

    @Nested
    @DisplayName("토큰 조회 테스트")
    class TokenSearchTest {
        @Test
        @DisplayName("토큰 조회 성공")
        public void success_token_search() {
            // initialize
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String hash = "hash1";
            FCMToken fcmToken = FCMToken.builder()
                    .email(email)
                    .tokens(List.of(Token.builder()
                            .deviceUUID(uuid)
                            .hash(hash)
                            .build()))
                    .build();
            // given
            given(fcmTokenRepository.findById(anyString()))
                    .willReturn(Optional.of(fcmToken));

            // when
            Token expected = fcmTokenService.search(email, uuid);

            // then
            assertEquals(expected.getDeviceUUID(), uuid);
            assertEquals(expected.getHash(), hash);
        }

        @Test
        @DisplayName("토큰 조회 실패 - 토큰이 등록된 적 없는 경우")
        public void fail_search_token_because_not_registered() {
            // initialize
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();


            // given
            given(fcmTokenRepository.findById(anyString()))
                    .willThrow(new FCMTokenException(NO_SUCH_TOKEN));

            // when
            FCMTokenException fcmTokenException = assertThrows(FCMTokenException.class, () -> fcmTokenService.search(email, uuid));

            // then
            assertEquals(fcmTokenException.getErrorCode(), NO_SUCH_TOKEN.name());
            assertEquals(fcmTokenException.getDescription(), NO_SUCH_TOKEN.getDescription());
        }

        @Test
        @DisplayName("토큰 조회 실패 - 기기의 uuid가 등록된 적 없는 경우")
        public void fail_search_token_because_not_registered_uuid() {
            // initialize
            String email = "email@gmail.com";
            String uuid = UUID.randomUUID().toString();
            String hash = "hash1";
            FCMToken fcmToken = FCMToken.builder()
                    .email(email)
                    .tokens(List.of(Token.builder()
                            .deviceUUID(uuid)
                            .hash(hash)
                            .build()))
                    .build();
            String requestedUUID = UUID.randomUUID().toString();
            // given
            given(fcmTokenRepository.findById(anyString()))
                    .willReturn(Optional.of(fcmToken));

            // when
            FCMTokenException fcmTokenException = assertThrows(FCMTokenException.class, () -> fcmTokenService.search(email, requestedUUID));

            // then
            assertEquals(fcmTokenException.getErrorCode(), NO_SUCH_TOKEN.name());
            assertEquals(fcmTokenException.getDescription(), NO_SUCH_TOKEN.getDescription());
        }


    }
}