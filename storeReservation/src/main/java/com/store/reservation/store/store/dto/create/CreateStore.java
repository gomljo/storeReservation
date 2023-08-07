package com.store.reservation.store.store.dto.create;

import com.store.reservation.store.store.domain.model.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateStore {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private long memberId;
        private String storeName;
        private String roadName;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String storeName;

        public static CreateStore.Response fromEntity(Store store) {
            return Response.builder()
                    .storeName(store.getStoreName())
                    .build();
        }
    }


}
