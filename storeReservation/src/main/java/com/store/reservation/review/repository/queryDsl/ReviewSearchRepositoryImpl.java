package com.store.reservation.review.repository.queryDsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.reservation.global.review.dto.ReviewSearchDto;
import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.review.domain.Review;
import com.store.reservation.review.repository.constant.ComparatorFlags;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.store.reservation.reservation.constants.search.Duration.*;
import static com.store.reservation.review.domain.QReview.review;
import static com.store.reservation.review.repository.constant.ComparatorFlags.CREATE_AT;
import static com.store.reservation.review.repository.constant.ComparatorFlags.MODIFIED_AT;

@Repository
@RequiredArgsConstructor
public class ReviewSearchRepositoryImpl implements ReviewSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> searchReviewListForCustomerBy(Long customerId, ReviewSearchDto reviewSearchDto) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime limit = calculateLimit(reviewSearchDto.getDuration(), today);
        List<Review> reviewList = queryFactory.selectFrom(review)
                .where(review.memberInformation.id.eq(customerId)
                        .and(isModifiedDateNull()
                                .and(isInDuration(MODIFIED_AT, reviewSearchDto.getDuration(), limit,today))
                        .or(isInDuration(CREATE_AT, reviewSearchDto.getDuration(), limit, today))
                        )
                )
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(review)
                .where(review.memberInformation.id.eq(customerId)
                        .and(isModifiedDateNull()
                                .and(isInDuration(MODIFIED_AT, reviewSearchDto.getDuration(), limit,today))
                                .or(isInDuration(CREATE_AT, reviewSearchDto.getDuration(), limit, today))
                        )
                )
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetchFirst();
        return new PageImpl<>(reviewList, reviewSearchDto.getPageable(), count);
    }

    @Override
    public Page<Review> searchReviewListForManagerBy(Long storeId, ReviewSearchDto reviewSearchDto) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime limit = calculateLimit(reviewSearchDto.getDuration(), today);
        List<Review> reviewList = queryFactory.selectFrom(review)
                .where(review.store.id.eq(storeId)
                        .and(isModifiedDateNull()
                                .and(isInDuration(MODIFIED_AT, reviewSearchDto.getDuration(), limit,today))
                        .or(isInDuration(CREATE_AT, reviewSearchDto.getDuration(), limit, today))
                        )
                )
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetch();
        Long count = queryFactory.select(Wildcard.count)
                .from(review)
                .where(review.store.id.eq(storeId)
                        .and(isModifiedDateNull()
                                .and(isInDuration(MODIFIED_AT, reviewSearchDto.getDuration(), limit,today))
                        .or(isInDuration(CREATE_AT, reviewSearchDto.getDuration(), limit, today))
                    )
                )
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetchFirst();
        return new PageImpl<>(reviewList, reviewSearchDto.getPageable(), count);
    }
//    private NumberPath<Long> selectIdComparator(){
//
//    }

    private BooleanExpression isModifiedDateNull() {
        return review.modifiedAt.isNotNull();
    }

    private BooleanExpression isInDuration(ComparatorFlags flags, Duration duration, LocalDateTime limit, LocalDateTime today) {
        DateTimePath<LocalDateTime> comparator = selectComparator(flags);
        if (duration == TOTAL) {
            return comparator.eq(today).or(comparator.before(today));
        }
        return comparator.between(limit, today);
    }
    private DateTimePath<LocalDateTime> selectComparator(ComparatorFlags flags){
        if(flags== CREATE_AT){
            return review.createAt;
        }
        return review.modifiedAt;
    }
    private LocalDateTime calculateLimit(Duration duration, LocalDateTime today) {
        switch (duration.getTimeUnit()){
            case WEEK:
                return today.minusWeeks(duration.getHowManyPast());
            case MONTH:
                return today.minusMonths(duration.getHowManyPast());
            case YEAR:
                return today.minusYears(duration.getHowManyPast());
        }
        return today;
    }

}
