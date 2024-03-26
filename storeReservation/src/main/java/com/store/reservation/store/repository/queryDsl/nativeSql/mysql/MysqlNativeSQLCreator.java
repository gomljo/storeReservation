package com.store.reservation.store.repository.queryDsl.nativeSql.mysql;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.store.reservation.store.repository.queryDsl.nativeSql.NativeSqlCreator;
import org.springframework.stereotype.Component;

@Component
public class MysqlNativeSQLCreator implements NativeSqlCreator {
    @Override
    public NumberExpression<Double> createNativeRadiusQuery(Double longitude,
                                                            Double latitude,
                                                            NumberPath<Double> storeLongitude,
                                                            NumberPath<Double> storeLatitude) {
        return Expressions.numberTemplate(Double.class, "ST_Distance_Sphere({0}, {1})",
                Expressions.stringTemplate(
                        "POINT({0}, {1})",
                        latitude,
                        longitude
                ),
                Expressions.stringTemplate(
                        "POINT({0}, {1})",
                        storeLatitude,
                        storeLongitude
                ));
    }
}
