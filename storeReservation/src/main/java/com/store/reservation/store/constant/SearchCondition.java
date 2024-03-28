package com.store.reservation.store.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SearchCondition {
    DISTANCE,
    REVIEW,
    RATING
    ;
}
