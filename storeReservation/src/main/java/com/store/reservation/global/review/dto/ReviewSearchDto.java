package com.store.reservation.global.review.dto;

import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.reservation.constants.search.SortOption;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(name = "ReviewSearchDto")
public class ReviewSearchDto {
    @NotNull
    @Schema(name = "조회 기간")
    private Duration duration;
    @NotNull
    @Schema(name = "정렬 순서")
    private SortOption sortOption;
    @NotNull
    @Schema(name = "페이징 관련 정보")
    private Pageable pageable;
}
