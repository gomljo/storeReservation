package com.store.reservation.image.dto;

import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.model.UsageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ImageIdentificationDto {
    private UsageType usageType;
    private DomainType domainType;
    private Long domainId;
}
