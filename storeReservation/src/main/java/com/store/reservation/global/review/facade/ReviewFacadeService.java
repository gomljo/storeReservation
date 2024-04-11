package com.store.reservation.global.review.facade;

import com.store.reservation.aop.lock.annotation.DistributedLock;
import com.store.reservation.common.dto.SearchResponse;
import com.store.reservation.global.review.dto.ReviewDetailDto;
import com.store.reservation.global.review.dto.ReviewDto;
import com.store.reservation.global.review.dto.ReviewSearchDto;
import com.store.reservation.member.model.SecurityUser;
import com.store.reservation.reservation.domain.model.Reservation;
import com.store.reservation.reservation.service.ReservationService;
import com.store.reservation.review.domain.Review;
import com.store.reservation.review.exception.ReviewError;
import com.store.reservation.review.exception.ReviewRuntimeException;
import com.store.reservation.review.service.ReviewService;
import com.store.reservation.store.domain.model.Store;
import com.store.reservation.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.store.reservation.review.exception.ReviewError.ILLEGAL_ACCESS;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewFacadeService {

    private final ReviewService reviewService;
    private final ReservationService reservationService;
    private final StoreService storeService;

    @DistributedLock(group = "review", key = "#reservationId")
    public void writeReview(Long customerId, Long reservationId, ReviewDto reviewDto) {
        Reservation reservation = reservationService.searchBy(reservationId);

        if (!reservation.getCustomer().isSame(customerId)) {
            throw new ReviewRuntimeException(ReviewError.ILLEGAL_ACCESS);
        }

        if (!reservation.isApproval() && !reservation.isArrived()) {
            throw new ReviewRuntimeException(ILLEGAL_ACCESS);
        }

        Store store = reservation.getStore();
        store.increaseReviewCount();
        store.increaseStarCount(reviewDto.getStarRating());

        reviewService.save(Review.builder()
                .memberInformation(reservation.getCustomer())
                .store(store)
                .content(reviewDto.getContent())
                .starRating(reviewDto.getStarRating())
                .build());
    }

    public void modifyReview(Long customerId, Long reviewId, ReviewDto reviewDto) {
        Review review = reviewService.searchBy(reviewId);
        int previousStarRating = review.getStarRating();
        if (!review.getMemberInformation().isSame(customerId)) {
            throw new ReviewRuntimeException(ILLEGAL_ACCESS);
        }
        review.update(reviewDto.getStarRating(), reviewDto.getContent());
        Store store = review.getStore();
        store.changeStarCount(previousStarRating, review.getStarRating());
    }

    public SearchResponse<ReviewDetailDto> searchReviewListForCustomerBy(SecurityUser customerAuth, Long customerId,
                                                                         ReviewSearchDto reviewSearchDto) {
        if (customerAuth.isNotSameUser(customerId)) {
            throw new ReviewRuntimeException(ILLEGAL_ACCESS);
        }
        return SearchResponse.from(reviewService.searchReviewListForCustomerBy(customerId, reviewSearchDto),
                ReviewDetailDto::from);
    }

    public SearchResponse<ReviewDetailDto> searchReviewListForManagerBy(SecurityUser managerAuth, Long storeId, ReviewSearchDto reviewSearchDto) {
        Store store = storeService.searchStoreByCustomer(storeId);
        if (managerAuth.isNotSameUser(store.getMemberInformation().getId())) {
            throw new ReviewRuntimeException(ILLEGAL_ACCESS);
        }
        return SearchResponse.from(reviewService.searchReviewListForManagerBy(storeId, reviewSearchDto),
                ReviewDetailDto::from);
    }
}
