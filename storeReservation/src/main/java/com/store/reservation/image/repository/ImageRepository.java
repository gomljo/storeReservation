package com.store.reservation.image.repository;

import com.store.reservation.image.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByDomainId(Long entityId);
}
