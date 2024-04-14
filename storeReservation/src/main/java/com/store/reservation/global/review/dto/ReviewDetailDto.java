package com.store.reservation.global.review.dto;

import com.store.reservation.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(name = "ReviewDetailDto")
public class ReviewDetailDto {
    @Min(1)
    @Schema(name = "후기 Id")
    private Long reviewId;
    @NotNull
    @Schema(name = "후기 내용")
    private String content;
    @Min(1)
    @Max(5)
    @Schema(name = "별점")
    private Integer starRating;


    public static ReviewDetailDto from(Review review) {
        return ReviewDetailDto.builder()
                .reviewId(review.getId())
                .content(review.getContent())
                .starRating(review.getStarRating())
                .build();
    }
}
