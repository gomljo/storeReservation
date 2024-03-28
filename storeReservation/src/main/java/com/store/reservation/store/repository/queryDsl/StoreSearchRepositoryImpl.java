package com.store.reservation.store.repository.queryDsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.repository.queryDsl.dto.SearchDto;
import com.store.reservation.store.repository.queryDsl.nativeSql.NativeSqlCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.store.reservation.store.domain.model.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreSearchRepositoryImpl implements StoreSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final NativeSqlCreator mysqlNativeSQLCreator;

    public Page<Store> searchStoreByCondition(SearchDto searchDto) {
        // search type 별 팩토리 패턴을 써서 order by를 건다.
        OrderSpecifier<?> sorting = getSortOptions(searchDto);
        List<Store> storeEntityInRadiusList = jpaQueryFactory
                .select(Projections.bean(Store.class))
                .from(store)
                .where(isStoreInRadius(searchDto))
                .orderBy(sorting)
                .offset(searchDto.getPageable().getOffset())
                .limit(searchDto.getPageable().getPageSize() + 1)
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(store)
                .fetchFirst();

        return new PageImpl<>(storeEntityInRadiusList, searchDto.getPageable(), count);
    }

    @Override
    public boolean existsStoreByRoadNameAndStoreName(String roadName, String storeName) {
        Integer fetchResult = jpaQueryFactory.selectOne()
                .from(store)
                .where(store.location.roadName.eq(roadName)
                        .and(store.storeName.eq(storeName)))
                .fetchFirst();
        return !Objects.isNull(fetchResult);
    }

    private BooleanExpression isStoreInRadius(SearchDto searchDto) {
        if (Objects.isNull(searchDto)) {
            throw new NullPointerException("조회할 위치 정보가 없습니다.");
        }
        return getCalcDistanceNativeSQL(searchDto)
                .loe(searchDto.getRadius());
    }

    private NumberExpression<Double> getCalcDistanceNativeSQL(SearchDto searchDto) {
        return mysqlNativeSQLCreator.createNativeRadiusQuery(
                searchDto.getLongitude(), searchDto.getLatitude(),
                store.location.lnt, store.location.lat
        );
    }

    private OrderSpecifier<Double> distanceAsc(SearchDto searchDto) {
        return getCalcDistanceNativeSQL(searchDto).asc();
    }

    private OrderSpecifier<Double> starRatingDesc() {
        return store.starRating.desc();
    }

    private OrderSpecifier<Long> numberOfReviewsDesc() {
        return store.numberOfReviews.desc();
    }

    private OrderSpecifier<?> getSortOptions(SearchDto searchDto) {
        switch (searchDto.getSearchCondition()) {
            case RATING:
                return starRatingDesc();
            case REVIEW:
                return numberOfReviewsDesc();

        }
        return distanceAsc(searchDto);
    }
}
