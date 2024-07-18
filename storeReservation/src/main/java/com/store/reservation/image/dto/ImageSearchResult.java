package com.store.reservation.image.dto;

import com.store.reservation.image.model.ImageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageSearchResult {
    private Long imageId;
    private String imageUrl;

    public static ImageSearchResult from(ImageEntity image) {
        return new ImageSearchResult(image.getId(), image.getUrl());
    }
}
