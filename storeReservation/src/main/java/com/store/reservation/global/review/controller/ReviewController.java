package com.store.reservation.global.review.controller;

import com.store.reservation.common.dto.SearchResponse;
import com.store.reservation.global.review.dto.ReviewDetailDto;
import com.store.reservation.global.review.dto.ReviewDto;
import com.store.reservation.global.review.dto.ReviewSearchDto;
import com.store.reservation.global.review.facade.ReviewFacadeService;
import com.store.reservation.global.review.state.ReviewStatusCode;
import com.store.reservation.member.model.SecurityUser;
import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.reservation.constants.search.SortOption;
import com.store.reservation.reservation.validator.duration.DurationCheck;
import com.store.reservation.reservation.validator.sortOption.SortOptionCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static com.store.reservation.global.review.state.ReviewStatusCode.MODIFY_SUCCESS;
import static com.store.reservation.global.review.state.ReviewStatusCode.WRITE_SUCCESS;

@Tag(name = "reviews")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacadeService reviewFacadeService;

    @Operation(summary = "리뷰 작성",
            description = "사용자로부터 작성된 리뷰 내용과 별점을 이용하여 리뷰를 저장하고 매장의 별점 정보를 갱신",
            tags = "reviews")
    @PostMapping("/{reservationId}")
    public ResponseEntity<ReviewStatusCode> write(@AuthenticationPrincipal SecurityUser securityUser,
                                                  @Parameter(name = "reservationId", description = "리뷰를 작성하고자 하는 예약의 id",
                                                          in = ParameterIn.PATH)
                                                  @PathVariable Long reservationId,
                                                  @RequestBody ReviewDto reviewDto) {
        reviewFacadeService.writeReview(securityUser.getId(), reservationId, reviewDto);
        return ResponseEntity.ok(WRITE_SUCCESS);
    }

    @Operation(summary = "리뷰 수정",
            description = "변경된 리뷰 내용과 별점을 이용하여 리뷰 갱신",
            tags = "reviews")
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewStatusCode> modify(@AuthenticationPrincipal SecurityUser securityUser,
                                                   @Parameter(name = "reviewId", description = "변경을 원하는 리뷰 id",
                                                           in = ParameterIn.PATH)
                                                   @PathVariable Long reviewId,
                                                   @RequestBody ReviewDto reviewDto) {
        reviewFacadeService.modifyReview(securityUser.getId(), reviewId, reviewDto);
        return ResponseEntity.ok(MODIFY_SUCCESS);
    }

    @Operation(summary = "고객이 작성한 리뷰 목록 조회",
            description = "고객이 작성한 리뷰 목록을 조회 기간, 정렬 순서, 페이지 번호와 페이지 당 크기 정보를 통해 조회",
            tags = "reviews")
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<SearchResponse<ReviewDetailDto>> searchReviewListForCustomerBy(@AuthenticationPrincipal
                                                                                         SecurityUser customer,
                                                                                         @Parameter(name = "customerId",
                                                                                                 description = "조회를 원하는 고객의 id",
                                                                                                 in = ParameterIn.PATH)
                                                                                         @PathVariable Long customerId,
                                                                                         @Parameter(name = "duration",
                                                                                                 description = "조회 기간",
                                                                                                 in = ParameterIn.QUERY)
                                                                                         @RequestParam @DurationCheck Duration duration,
                                                                                         @Parameter(name = "sortOption",
                                                                                                 description = "정렬 순서",
                                                                                                 in = ParameterIn.QUERY)
                                                                                         @RequestParam @SortOptionCheck SortOption sortOption,
                                                                                         @Parameter(name = "pageIndex",
                                                                                                 description = "페이지 번호",
                                                                                                 in = ParameterIn.QUERY)
                                                                                         @RequestParam @Min(0) int pageIndex,
                                                                                         @Parameter(name = "pageSize",
                                                                                                 description = "페이지 당 크기",
                                                                                                 in = ParameterIn.QUERY)
                                                                                         @RequestParam @Min(10) @Max(30) int pageSize) {

        return ResponseEntity.ok(SearchResponse.from(reviewFacadeService.searchReviewListForCustomerBy(customer, customerId,
                ReviewSearchDto.builder()
                        .duration(duration)
                        .sortOption(sortOption)
                        .pageable(PageRequest.of(pageIndex, pageSize))
                        .build()
        ), ReviewDetailDto::from));
    }

    @Operation(tags = "reviews")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<SearchResponse<ReviewDetailDto>> searchReviewListForManagerBy(@AuthenticationPrincipal SecurityUser manager,
                                                                                        @Parameter(name = "storeId",
                                                                                                description = "조회를 원하는 매장의 id",
                                                                                                in = ParameterIn.PATH)
                                                                                        @PathVariable Long storeId,
                                                                                        @Parameter(name = "duration",
                                                                                                description = "조회 기간",
                                                                                                in = ParameterIn.QUERY)
                                                                                        @RequestParam @DurationCheck Duration duration,
                                                                                        @Parameter(name = "sortOption",
                                                                                                description = "정렬 순서",
                                                                                                in = ParameterIn.QUERY)
                                                                                        @RequestParam @SortOptionCheck SortOption sortOption,
                                                                                        @Parameter(name = "pageIndex",
                                                                                                description = "페이지 번호",
                                                                                                in = ParameterIn.QUERY)
                                                                                        @RequestParam @Min(0) int pageIndex,
                                                                                        @Parameter(name = "pageSize",
                                                                                                description = "페이지 당 크기",
                                                                                                in = ParameterIn.QUERY)
                                                                                        @RequestParam @Min(10) @Max(30) int pageSize) {
        return ResponseEntity.ok(SearchResponse.from(reviewFacadeService.searchReviewListForManagerBy(manager, storeId,
                ReviewSearchDto.builder()
                        .duration(duration)
                        .sortOption(sortOption)
                        .pageable(PageRequest.of(pageIndex, pageSize))
                        .build()), ReviewDetailDto::from));
    }

}
