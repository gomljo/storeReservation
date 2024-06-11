package com.store.reservation.image.util;

import com.store.reservation.image.model.DomainType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageClient {

    String upload(DomainType domainType, Long id, MultipartFile multipartFiles);

    void delete(String identifier);

    List<String> changeFileName(List<String> storedUrls);

}
