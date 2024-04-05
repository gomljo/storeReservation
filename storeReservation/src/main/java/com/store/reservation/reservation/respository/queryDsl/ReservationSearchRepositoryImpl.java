package com.store.reservation.reservation.respository.queryDsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.reservation.constants.search.SortOption;
import com.store.reservation.reservation.constants.state.ReservationState;
import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.dto.ReservationSearchDto;
import com.store.reservation.reservation.dto.common.ReservationTimeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.store.reservation.reservation.constants.search.Duration.*;
import static com.store.reservation.reservation.domain.model.QReservation.reservation;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReservationSearchRepositoryImpl implements ReservationSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public int aggregateNumberOfReservationsBy(Long storeId, ReservationTimeDto reservationTimeDto) {
        return Math.toIntExact(
                jpaQueryFactory
                        .select(reservation.count())
                        .from(reservation)
                        .where(reservation.store.id.eq(storeId)
                                .and(reservation.time.reservedDate.eq(reservationTimeDto.getReservationDate()))
                                .and(reservation.time.reservedTime.eq(reservationTimeDto.getReservationTime()))
                                .and(reservation.state.reservationState.ne(ReservationState.DENIAL)))
                        .fetchFirst());
    }

    @Override
    public Page<Reservation> findReservationListByStore(Long storeId, ReservationSearchDto reservationSearchDto) {

        List<Reservation> reservationList = jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.store.id.eq(storeId)
                        .and(durationSelector(LocalDate.now(), reservationSearchDto.getDuration()))
                        .and(reservation.state.reservationState.eq(reservationSearchDto.getReservationState()))
                )
                .orderBy(orderingFilter(reservationSearchDto.getSortOption()))
                .offset(reservationSearchDto.getPageable().getOffset())
                .limit(reservationSearchDto.getPageable().getPageSize() + 1)
                .fetch();
        Long count = jpaQueryFactory
                .select(reservation.count())
                .from(reservation)
                .where(reservation.store.id.eq(storeId)
                        .and(durationSelector(LocalDate.now(), reservationSearchDto.getDuration()))
                        .and(reservation.state.reservationState.eq(reservationSearchDto.getReservationState()))
                )
                .orderBy(orderingFilter(reservationSearchDto.getSortOption()))
                .offset(reservationSearchDto.getPageable().getOffset())
                .limit(reservationSearchDto.getPageable().getPageSize() + 1)
                .fetchFirst();

        return new PageImpl<>(reservationList, reservationSearchDto.getPageable(), count);
    }

    @Override
    public Page<Reservation> findReservationListByCustomer(Long customerId, ReservationSearchDto reservationSearchDto) {

        List<Reservation> reservationList = jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.customer.id.eq(customerId)
                        .and(durationSelector(LocalDate.now(), reservationSearchDto.getDuration()))
                        .and(reservation.state.reservationState.eq(reservationSearchDto.getReservationState()))
                )
                .orderBy(orderingFilter(reservationSearchDto.getSortOption()))
                .offset(reservationSearchDto.getPageable().getOffset())
                .limit(reservationSearchDto.getPageable().getPageSize() + 1)
                .fetch();
        Long count = jpaQueryFactory
                .select(reservation.count())
                .from(reservation)
                .where(reservation.customer.id.eq(customerId)
                        .and(durationSelector(LocalDate.now(), reservationSearchDto.getDuration()))
                        .and(reservation.state.reservationState.eq(reservationSearchDto.getReservationState()))
                )
                .orderBy(orderingFilter(reservationSearchDto.getSortOption()))
                .offset(reservationSearchDto.getPageable().getOffset())
                .limit(reservationSearchDto.getPageable().getPageSize() + 1)
                .fetchFirst();

        return new PageImpl<>(reservationList, reservationSearchDto.getPageable(), count);
    }

    @Override
    public Page<Reservation> findReservationListByStoreAndDateAndTime(Long storeId, ReservationTimeDto reservationTimeDto,
                                                                      Pageable pageable) {

        List<Reservation> reservationList = jpaQueryFactory
                .selectFrom(reservation)
                .where(reservation.store.id.eq(storeId)
                        .and(reservation.time.reservedDate.eq(reservationTimeDto.getReservationDate())
                                .and(reservation.time.reservedTime.eq(reservationTimeDto.getReservationTime()))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        Long count = jpaQueryFactory
                .select(Wildcard.count)
                .from(reservation)
                .where(reservation.store.id.eq(storeId)
                        .and(reservation.time.reservedDate.eq(reservationTimeDto.getReservationDate())
                                .and(reservation.time.reservedTime.eq(reservationTimeDto.getReservationTime()))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetchFirst();
        return new PageImpl<>(reservationList, pageable, count);
    }

    private BooleanExpression durationSelector(LocalDate standard, Duration duration) {
        switch (duration) {
            case THIS_WEEK:
                LocalDate past = standard.minusDays(THIS_WEEK.getHowManyPast());
                return reservation.time.reservedDate.before(standard).and(reservation.time.reservedDate.after(past));
            case ONE_MONTH:
                return reservation.time.reservedDate.between(standard.minusMonths(ONE_MONTH.getHowManyPast()), standard);
            case THREE_MONTH:
                return reservation.time.reservedDate.between(standard.minusMonths(THREE_MONTH.getHowManyPast()), standard);
        }
        return reservation.time.reservedDate.loe(standard);
    }

    private OrderSpecifier<LocalDate> orderingFilter(SortOption sortOption) {
        if (sortOption == SortOption.ASCENDING) {
            return reservation.time.reservedDate.asc();
        }
        return reservation.time.reservedDate.desc();
    }

    private DateTemplate<LocalDate> convertToDateFormat(LocalDate localDate) {
        return Expressions.dateTemplate(LocalDate.class, "DATE_FORMAT({0},{1})", localDate, "yyyy-mm-dd");
    }
}
