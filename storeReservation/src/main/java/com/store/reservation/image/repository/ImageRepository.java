package com.store.reservation.image.repository;

import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByDomainTypeAndDomainId(DomainType domainType, Long domainId);

}
