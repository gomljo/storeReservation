package com.store.reservation.global.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Schema(description = "리뷰 내용 및 별점 정보를 갖는 리뷰 작성 DTO")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewDto {
    @NotNull
    @Schema(description = "리뷰 내용")
    private String content;
    @Min(0)
    @Max(5)
    @Schema(description = "별점", allowableValues = {"0","1", "2", "3", "4", "5"})
    private int starRating;
}
