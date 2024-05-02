package com.store.reservation.notification.token.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Document
public class Token {
    @Id
    private String deviceUUID;
    private String hash;

    public boolean match(String deviceUUID) {
        return this.deviceUUID.equals(deviceUUID);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token otherToken = (Token) obj;
        return this.deviceUUID.equals(otherToken.getDeviceUUID());
    }
}
