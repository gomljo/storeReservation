package com.store.reservation.image.util;

import com.store.reservation.image.model.DomainType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageClient {

    String upload(DomainType domainType, Long id, MultipartFile multipartFiles);

    void delete(String identifier);


}
