package com.store.reservation.store.repository.queryDsl.dto;

import com.store.reservation.store.constant.SearchCondition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SearchDto {
    private double latitude;
    private double longitude;
    private double radius;
    private SearchCondition searchCondition;
    private Pageable pageable;
}
