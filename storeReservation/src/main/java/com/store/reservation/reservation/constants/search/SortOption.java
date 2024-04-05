package com.store.reservation.reservation.constants.search;

import java.util.Arrays;

public enum SortOption {
    ASCENDING,
    DESCENDING
    ;

    public static boolean validate(String value){
        return Arrays.stream(SortOption.values()).anyMatch(sortOption -> sortOption.name().equals(value));
    }
}
