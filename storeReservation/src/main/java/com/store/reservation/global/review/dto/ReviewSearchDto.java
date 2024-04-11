package com.store.reservation.global.review.dto;

import com.store.reservation.reservation.constants.search.Duration;
import com.store.reservation.reservation.constants.search.SortOption;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReviewSearchDto {
    private Duration duration;
    private SortOption sortOption;
    private Pageable pageable;
}
