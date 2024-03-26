package com.store.reservation.store.repository.queryDsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.dto.search.request.SearchStoreDto;
import com.store.reservation.store.repository.queryDsl.nativeSql.NativeSqlCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import static com.store.reservation.store.domain.model.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreSearchRepositoryImpl implements StoreSearchRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final NativeSqlCreator mysqlNativeSQLCreator;

    // TODO: 2024-02-26
    // predicate 객체를 switch case 구문으로 리턴하도록 변경
    public Page<Store> searchStoreByCondition(SearchStoreDto searchStoreDto, Pageable pageable) {
        // search type 별 팩토리 패턴을 써서 order by를 건다.
        OrderSpecifier<?> sorting = getSortOptions(searchStoreDto);
        List<Store> storeEntityInRadiusList = jpaQueryFactory
                .select(Projections.bean(Store.class))
                .from(store)
                .where(isStoreInRadius(searchStoreDto))
                .orderBy(sorting)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Long count = jpaQueryFactory.select(Wildcard.count)
                .from(store)
                .fetchFirst();

        return new PageImpl<>(storeEntityInRadiusList, pageable, count);
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

    private BooleanExpression isStoreInRadius(SearchStoreDto searchStoreDto) {
        if (Objects.isNull(searchStoreDto)) {
            throw new NullPointerException("조회할 위치 정보가 없습니다.");
        }
        return getCalcDistanceNativeSQL(searchStoreDto)
                .loe(searchStoreDto.getRadius());
    }

    private NumberExpression<Double> getCalcDistanceNativeSQL(SearchStoreDto searchStoreDto) {
        return mysqlNativeSQLCreator.createNativeRadiusQuery(
                searchStoreDto.getLongitude(), searchStoreDto.getLatitude(),
                store.location.lnt, store.location.lat
        );
    }

    private OrderSpecifier<Double> distanceAsc(SearchStoreDto searchStoreDto) {
        return getCalcDistanceNativeSQL(searchStoreDto).asc();
    }

    private OrderSpecifier<Double> starRatingDesc() {
        return store.starRating.desc();
    }
    private OrderSpecifier<Long> numberOfReviewsDesc(){
        return store.numberOfReviews.desc();
    }

    private OrderSpecifier<?> getSortOptions(SearchStoreDto searchStoreDto) {
        switch (searchStoreDto.getSearchCondition()) {
            case RATING:
                return starRatingDesc();
            case REVIEW:
                return numberOfReviewsDesc();

        }
        return distanceAsc(searchStoreDto);
    }
}
