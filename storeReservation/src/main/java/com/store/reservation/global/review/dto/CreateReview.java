package com.store.reservation.global.review.dto;

import com.store.reservation.review.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateReview {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String review;
        private int starRating;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String review;

        public static CreateReview.Response fromEntity(Review review){
            return new Response(review.getReview());
        }
    }
}
