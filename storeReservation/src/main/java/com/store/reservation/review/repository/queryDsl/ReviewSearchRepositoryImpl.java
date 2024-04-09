package com.store.reservation.review.repository.queryDsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.reservation.global.review.dto.ReviewSearchDto;
import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.store.reservation.reservation.constants.search.Duration.*;
import static com.store.reservation.review.domain.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewSearchRepositoryImpl implements ReviewSearchRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Review> searchReviewListForCustomerBy(Long customerId, ReviewSearchDto reviewSearchDto) {
        List<Review> reviewList = queryFactory.selectFrom(review)
                .where(review.memberInformation.id.eq(customerId)
                        .and(durationSelector(reviewSearchDto.getDuration(), LocalDateTime.now())))
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(review)
                .where(review.memberInformation.id.eq(customerId)
                        .and(durationSelector(reviewSearchDto.getDuration(), LocalDateTime.now())))
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetchFirst();
        return new PageImpl<>(reviewList, reviewSearchDto.getPageable(), count);
    }

    @Override
    public Page<Review> searchReviewListForManagerBy(Long storeId, ReviewSearchDto reviewSearchDto) {
        List<Review> reviewList = queryFactory.selectFrom(review)
                .where(review.store.id.eq(storeId).and(durationSelector(reviewSearchDto.getDuration(), LocalDateTime.now())))
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetch();
        Long count = queryFactory.select(Wildcard.count)
                .from(review)
                .where(review.store.id.eq(storeId).and(durationSelector(reviewSearchDto.getDuration(), LocalDateTime.now())))
                .offset(reviewSearchDto.getPageable().getOffset())
                .limit(reviewSearchDto.getPageable().getPageSize() + 1)
                .fetchFirst();
        return new PageImpl<>(reviewList, reviewSearchDto.getPageable(), count);
    }


    private BooleanExpression durationSelector(Duration duration, LocalDateTime today) {
        switch (duration) {

            case ONE_YEAR:
                return review.modifiedAt.isNotNull().and(review.modifiedAt.between(today.minusYears(ONE_YEAR.getHowManyPast()), today))
                        .or(review.createAt.between(today.minusYears(ONE_YEAR.getHowManyPast()), today));
            case SIX_MONTH:
                return review.modifiedAt.isNotNull().and(review.modifiedAt.between(today.minusMonths(SIX_MONTH.getHowManyPast()), today))
                        .or(review.createAt.between(today.minusMonths(SIX_MONTH.getHowManyPast()), today));
            case THREE_MONTH:
                return review.modifiedAt.isNotNull().and(review.modifiedAt.between(today.minusMonths(THREE_MONTH.getHowManyPast()), today))
                        .or(review.createAt.between(today.minusMonths(SIX_MONTH.getHowManyPast()), today));
            case ONE_MONTH:
                return review.modifiedAt.isNotNull().and(review.modifiedAt.between(today.minusMonths(ONE_MONTH.getHowManyPast()), today))
                        .or(review.createAt.between(today.minusMonths(ONE_MONTH.getHowManyPast()), today));
            case TOTAL:
                return review.modifiedAt.isNotNull().and(review.modifiedAt.eq(today).or(review.modifiedAt.before(today)))
                        .or(review.createAt.eq(today).or(review.createAt.before(today)))
                        ;
        }
        return review.modifiedAt.isNotNull().and(review.modifiedAt.between(today.minusDays(THIS_WEEK.getHowManyPast()), today))
                .or(review.createAt.between(today.minusDays(THIS_WEEK.getHowManyPast()), today));

    }
}
