package com.store.reservation.notification.token.repository;

import com.store.reservation.notification.token.document.FCMToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface FCMTokenRepository extends MongoRepository<FCMToken, String> {
    Optional<FCMToken> findByEmail(String email);
    boolean existsByEmail(String email);
}
