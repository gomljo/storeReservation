package com.store.reservation.review.service;

import com.store.reservation.review.domain.Review;
import com.store.reservation.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    public Review save(Review review){
        log.info("[ReviewService]: 데이터베이스에 리뷰 저장 시작");
        log.info("[ReviewService]: 데이터베이스에 리뷰 저장 완료");
        return reviewRepository.save(review);
    }
}
