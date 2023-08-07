package com.store.reservation.global.review.service;

import com.store.reservation.global.review.dto.CreateReview;
import com.store.reservation.reservation.log.domain.ReservationLog;
import com.store.reservation.reservation.log.service.ReservationLogService;
import com.store.reservation.review.domain.Review;
import com.store.reservation.review.service.ReviewService;
import com.store.reservation.store.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewComponentService {

    private final ReviewService reviewService;
    private final ReservationLogService reservationLogService;
    private final StoreService storeService;

    public Review createReview(long logId, CreateReview.Request request) {
        log.info("[ReviewService]: 리뷰 저장 시작");
        ReservationLog reservationLog = reservationLogService.searchBy(logId);
        storeService.updateStarRating(reservationLog.getStore(), request.getStarRating());
        log.info("[ReviewService]: 리뷰 저장 정상 종료");
        return reviewService.save(Review.builder()
                .review(request.getReview())
                .starRating(request.getStarRating())
                .store(reservationLog.getStore())
                .build());
    }

}
