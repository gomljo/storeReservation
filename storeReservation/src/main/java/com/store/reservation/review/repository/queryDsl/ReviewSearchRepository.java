package com.store.reservation.review.repository.queryDsl;

import com.store.reservation.global.review.dto.ReviewSearchDto;
import com.store.reservation.review.domain.Review;
import org.springframework.data.domain.Page;

public interface ReviewSearchRepository {

    Page<Review> searchReviewListForCustomerBy(Long customerId, ReviewSearchDto reviewSearchDto);
    Page<Review> searchReviewListForManagerBy(Long storeId, ReviewSearchDto reviewSearchDto);

}
