package com.store.reservation.notification.token.repository;

import com.store.reservation.notification.token.document.FCMToken;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FCMTokenRepository extends ElasticsearchRepository<FCMToken, String> {
}
