package com.store.reservation.image.dto;

import com.store.reservation.image.model.DomainType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ImageUploadDto {

    private Long domainId;
    private DomainType domainType;
    private ImageDto imageDto;
}
