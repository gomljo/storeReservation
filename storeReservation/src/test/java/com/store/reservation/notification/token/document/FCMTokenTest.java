package com.store.reservation.notification.token.document;

import com.store.reservation.notification.token.exception.FCMTokenErrorCode;
import com.store.reservation.notification.token.exception.FCMTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FCMTokenTest {
    private FCMToken fcmToken;
    private final UUID deviceUUID1 = UUID.randomUUID();
    private final UUID deviceUUID2 = UUID.randomUUID();
    private final String hashValue1 = "hash1";
    private final String hashValue2 = "hash2";

    @BeforeEach
    void setup() {

        fcmToken = new FCMToken("1234@gmail.com",
                List.of(Token.builder()
                                .deviceUUID(deviceUUID1.toString())
                                .hash(hashValue1)
                                .build(),
                        Token.builder()
                                .deviceUUID(deviceUUID2.toString())
                                .hash(hashValue2)
                                .build()));
    }

    @Test
    @DisplayName("기기의 UUID 값에 일치하는 토큰 값이 있다면 true를 반환한다.")
    public void success_get_token_has_matched_UUID() {
        Token token = fcmToken.getToken(deviceUUID1.toString());
        assertEquals(token.getHash(), hashValue1);
    }

    @Test
    @DisplayName("기기의 UUID 값과 일치하는 토큰 값이 없다면 예외가 발생한다.")
    public void fail_get_token_when_no_matched_UUID() {
        FCMTokenException fcmTokenException = assertThrows(FCMTokenException.class, () -> fcmToken.getToken(UUID.randomUUID().toString()));
        assertEquals(fcmTokenException.getDescription(), FCMTokenErrorCode.NO_SUCH_TOKEN.getDescription());
    }

    @Test
    @DisplayName("이미 등록된 기기의 토큰을 추가한다면 토큰은 추가되지 않는다.")
    public void do_not_add_token_because_already_exists(){
        int numberOfTokenBeforeAddition = fcmToken.getTokens().size();
        Token token = Token.builder()
                .deviceUUID(deviceUUID1.toString())
                .hash(hashValue2)
                .build();
        fcmToken.addToken(token);
        int numberOfTokenAfterAddition = fcmToken.getTokens().size();
        assertEquals(numberOfTokenBeforeAddition, numberOfTokenAfterAddition);
    }

    @Test
    @DisplayName("새로운 기기의 토큰을 추가한다면 토큰은 추가된다.")
    public void do_add_token_when_new_device_token_register(){
        String newDeviceUUID = UUID.randomUUID().toString();
        String newHashValue = "newHashValue";
        int numberOfTokenBeforeAddition = fcmToken.getTokens().size();
        Token token = Token.builder()
                .deviceUUID(newDeviceUUID)
                .hash(newHashValue)
                .build();
        fcmToken.addToken(token);
        int numberOfTokenAfterAddition = fcmToken.getTokens().size();
        assertEquals(numberOfTokenBeforeAddition+1, numberOfTokenAfterAddition);
        assertTrue(fcmToken.getTokens().contains(token));
    }

}