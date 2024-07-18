package com.store.reservation.image.verification.config;

import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.verification.strategies.ReviewVerification;
import com.store.reservation.image.verification.strategies.StoreVerification;
import com.store.reservation.image.verification.strategies.VerificationStrategy;
import com.store.reservation.image.verification.factory.VerificationStrategyFactory;
import com.store.reservation.review.repository.jpa.ReviewRepository;
import com.store.reservation.store.repository.jpa.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class VerificationStrategyFactoryConfig {


    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;


    public VerificationStrategyFactoryConfig(@Autowired StoreRepository storeRepository, @Autowired ReviewRepository reviewRepository) {
        this.storeRepository = storeRepository;
        this.reviewRepository = reviewRepository;
    }
    @Bean
    public StoreVerification storeVerificationStrategy(){
        return new StoreVerification(storeRepository);
    }

    @Bean
    public ReviewVerification reviewVerificationStrategy(){
        return new ReviewVerification(reviewRepository);
    }
    @Bean
    public VerificationStrategyFactory strategyFactory() {
        ConcurrentHashMap<String, VerificationStrategy> strategies = new ConcurrentHashMap<>();
        strategies.put(DomainType.STORE.name(), storeVerificationStrategy());
        strategies.put(DomainType.REVIEW.name(), reviewVerificationStrategy());

        return new VerificationStrategyFactory(strategies);
    }


}
