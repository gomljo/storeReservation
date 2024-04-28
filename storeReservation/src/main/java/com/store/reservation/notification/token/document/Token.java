package com.store.reservation.notification.token.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Token {
    @Field(type = FieldType.Keyword)
    private String deviceUUID;
    @Field(type = FieldType.Keyword)
    private String hash;

    public boolean match(String deviceUUID){
        return this.deviceUUID.equals(deviceUUID);
    }
}
