package com.store.reservation.image.verification.factory;

import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.verification.strategies.VerificationStrategy;

import java.util.concurrent.ConcurrentHashMap;


public class VerificationStrategyFactory {

    private final ConcurrentHashMap<String, VerificationStrategy> strategyFactory;

    public VerificationStrategyFactory(ConcurrentHashMap<String, VerificationStrategy> strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public VerificationStrategy getStrategy(DomainType domainType) {
        return strategyFactory.get(domainType.name());
    }

}
