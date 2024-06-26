package com.store.reservation.review.repository.jpa;

import com.store.reservation.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
