package com.store.reservation.image.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String url;
    @Enumerated(value = EnumType.STRING)
    private UsageType usageType;
    @Enumerated(value = EnumType.STRING)
    private DomainType domainType;
    private Long domainId;
}
