package com.store.reservation.image.model;

import com.store.reservation.image.util.ImageClient;
import com.store.reservation.member.model.BaseEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ImageEntity extends BaseEntity {

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

    public void change(ImageClient imageClient, MultipartFile multipartFile){
        imageClient.delete(this.url);
        this.url = imageClient.upload(this.domainType, domainId, multipartFile);
    }
    public void delete(ImageClient imageClient){
        imageClient.delete(this.url);
    }
}
