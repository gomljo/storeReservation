package com.store.reservation.review.service;

import com.store.reservation.global.review.dto.ReviewSearchDto;
import com.store.reservation.review.domain.Review;
import com.store.reservation.review.exception.ReviewRuntimeException;
import com.store.reservation.review.repository.jpa.ReviewRepository;
import com.store.reservation.review.repository.queryDsl.ReviewSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.store.reservation.review.exception.ReviewError.NO_SUCH_REVIEW;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewSearchRepository reviewSearchRepository;

    public void save(Review review) {
        reviewRepository.save(review);
    }

    public Review searchBy(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewRuntimeException(NO_SUCH_REVIEW));
    }

    public Page<Review> searchReviewListForCustomerBy(Long customerId, ReviewSearchDto reviewSearchDto) {
        return reviewSearchRepository.searchReviewListForCustomerBy(customerId, reviewSearchDto);
    }

    public Page<Review> searchReviewListForManagerBy(Long storeId, ReviewSearchDto reviewSearchDto) {
        return reviewSearchRepository.searchReviewListForManagerBy(storeId, reviewSearchDto);
    }
}
