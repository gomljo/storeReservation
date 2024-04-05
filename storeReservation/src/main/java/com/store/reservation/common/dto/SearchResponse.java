package com.store.reservation.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SearchResponse<T> {
    private long totalElements;
    private int totalPages;
    private boolean hasNextPage;
    @Builder.Default
    private List<T> content = new ArrayList<>();

    public static <T> SearchResponse<T> from(Page<T> pageResult) {
        return SearchResponse.<T>builder()
                .totalPages(pageResult.getTotalPages())
                .totalElements(pageResult.getTotalElements())
                .hasNextPage(pageResult.hasNext())
                .content(pageResult.getContent())
                .build();
    }

    public static <T, MapperType> SearchResponse<MapperType> from(Page<T> page, Function<? super T, ? extends MapperType> dtoMapper) {
        return SearchResponse.<MapperType>builder()
                .totalPages(page.getTotalPages())
                .hasNextPage(page.hasNext())
                .totalElements(page.getTotalElements())
                .content(page.getContent().stream().map(dtoMapper).collect(Collectors.toList()))
                .build();
    }
}
