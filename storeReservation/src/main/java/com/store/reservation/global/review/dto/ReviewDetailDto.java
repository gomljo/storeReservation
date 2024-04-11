package com.store.reservation.global.review.dto;

import com.store.reservation.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReviewDetailDto {

    private Long reviewId;
    private String content;
    private Integer starRating;


    public static ReviewDetailDto from(Review review) {
        return ReviewDetailDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .starRating(review.getStarRating())
                .build();
    }
}
