package com.store.reservation.notification.token.document;

import com.store.reservation.notification.token.exception.FCMTokenErrorCode;
import com.store.reservation.notification.token.exception.FCMTokenException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Document(collection = "token")
public class FCMToken {

    @Id
    private String email;
    @Builder.Default
    private List<Token> tokens = new ArrayList<>();


    public Token getToken(String deviceUUID) {
        return this.tokens.stream()
                .filter(token -> token.match(deviceUUID))
                .findFirst()
                .orElseThrow(() -> new FCMTokenException(FCMTokenErrorCode.NO_SUCH_TOKEN));
    }

    public void addToken(Token token) {
        List<Token> newTokens = new ArrayList<>(this.tokens);
        newTokens.add(token);
        this.tokens = newTokens;
    }

}
