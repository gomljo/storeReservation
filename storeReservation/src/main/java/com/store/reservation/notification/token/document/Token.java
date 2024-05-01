package com.store.reservation.notification.token.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Token {
    private String deviceUUID;
    private String hash;

    public boolean match(String deviceUUID){
        return this.deviceUUID.equals(deviceUUID);
    }
}
