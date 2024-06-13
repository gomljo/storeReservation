package com.store.reservation.image.service;

import com.store.reservation.image.dto.ImageDto;
import com.store.reservation.image.dto.ImageUploadDto;
import com.store.reservation.image.exception.ImageErrorCode;
import com.store.reservation.image.exception.ImageRuntimeException;
import com.store.reservation.image.model.Image;
import com.store.reservation.image.repository.ImageRepository;
import com.store.reservation.image.verification.strategies.VerificationStrategy;
import com.store.reservation.image.util.ImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageClient imageClient;

    public void upload(Long memberId, VerificationStrategy verificationStrategy,
                       ImageUploadDto imageUploadDto) {
        if (!verificationStrategy.verify(memberId, imageUploadDto.getDomainId())) {
            throw new ImageRuntimeException(ImageErrorCode.INVALID_ACCESS);
        }

        ImageDto imageDto = imageUploadDto.getImageDto();
        List<Image> imageList = imageDto.getImages().stream()
                .map(image -> this.imageClient.upload(imageUploadDto.getDomainType(),
                imageUploadDto.getDomainId(), image))
                .map(s3url -> Image.builder()
                                .url(s3url)
                                .domainType(imageUploadDto.getDomainType())
                                .usageType(imageDto.getUsageType())
                                .domainId(imageUploadDto.getDomainId())
                                .build())
                .collect(Collectors.toList());
        this.imageRepository.saveAll(imageList);
    }
}
