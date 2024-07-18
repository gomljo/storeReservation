package com.store.reservation.image.service;

import com.store.reservation.image.dto.ImageDto;
import com.store.reservation.image.dto.ImageUploadDto;
import com.store.reservation.image.exception.ImageErrorCode;
import com.store.reservation.image.exception.ImageRuntimeException;
import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.model.ImageEntity;
import com.store.reservation.image.repository.ImageRepository;
import com.store.reservation.image.util.ImageClient;
import com.store.reservation.image.verification.factory.VerificationStrategyFactory;
import com.store.reservation.image.verification.strategies.VerificationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageClient imageClient;
    private final VerificationStrategyFactory verificationStrategyFactory;

    public void upload(ImageUploadDto imageUploadDto) {
        VerificationStrategy verificationStrategy = verificationStrategyFactory.getStrategy(imageUploadDto.getDomainType());
        if (!verificationStrategy.verify(imageUploadDto.getUserId(), imageUploadDto.getDomainId())) {
            throw new ImageRuntimeException(ImageErrorCode.INVALID_ACCESS);
        }

        ImageDto imageDto = imageUploadDto.getImageDto();

        if (imageDto.getImages().isEmpty()) {
            throw new ImageRuntimeException(ImageErrorCode.EMPTY_IMAGES);
        }

        List<ImageEntity> imageEntityList = imageDto.getImages().stream()
                .map(image -> this.imageClient.upload(imageUploadDto.getDomainType(),
                        imageUploadDto.getDomainId(), image))
                .map(s3url -> ImageEntity.builder()
                        .url(s3url)
                        .domainType(imageUploadDto.getDomainType())
                        .usageType(imageDto.getUsageType())
                        .domainId(imageUploadDto.getDomainId())
                        .build())
                .collect(Collectors.toList());
        this.imageRepository.saveAll(imageEntityList);
    }

    public List<ImageEntity> searchBy(Long memberId, DomainType domainType, Long domainId) {
        VerificationStrategy verificationStrategy = verificationStrategyFactory.getStrategy(domainType);
        if (!verificationStrategy.verify(memberId, domainId)) {
            throw new ImageRuntimeException(ImageErrorCode.INVALID_ACCESS);
        }
        return imageRepository.findByDomainTypeAndDomainId(domainType, domainId);
    }

    public void delete(Long memberId, Long imageId) {
        ImageEntity imageEntity = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageRuntimeException(ImageErrorCode.NO_SUCH_IMAGE));

        VerificationStrategy verificationStrategy = verificationStrategyFactory.getStrategy(imageEntity.getDomainType());
        if (!verificationStrategy.verify(memberId, imageEntity.getDomainId())) {
            throw new ImageRuntimeException(ImageErrorCode.INVALID_ACCESS);
        }
        imageEntity.delete(imageClient);
        imageRepository.delete(imageEntity);
    }

    public Long update(Long memberId, Long imageId, MultipartFile image) {
        ImageEntity imageEntity = imageRepository.findById(imageId)
                .orElseThrow(() -> new ImageRuntimeException(ImageErrorCode.NO_SUCH_IMAGE));

        VerificationStrategy verificationStrategy = verificationStrategyFactory.getStrategy(imageEntity.getDomainType());
        if (!verificationStrategy.verify(memberId, imageEntity.getDomainId())) {
            throw new ImageRuntimeException(ImageErrorCode.INVALID_ACCESS);
        }
        imageEntity.change(imageClient, image);
        return imageId;
    }
}
