package com.store.reservation.store.repository.queryDsl.nativeSql;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;

public interface NativeSqlCreator {

    NumberExpression<Double> createNativeRadiusQuery(Double longitude,
                                                     Double latitude,
                                                     NumberPath<Double> storeLongitude,
                                                     NumberPath<Double> storeLatitude);
}
