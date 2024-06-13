package com.store.reservation.image.controller;

import com.store.reservation.apiResponse.ApiResponse;
import com.store.reservation.image.dto.ImageDto;
import com.store.reservation.image.dto.ImageIdentificationDto;
import com.store.reservation.image.dto.ImageUploadDto;
import com.store.reservation.image.service.ImageService;
import com.store.reservation.image.verification.factory.VerificationStrategyFactory;
import com.store.reservation.image.verification.strategies.VerificationStrategy;
import com.store.reservation.member.security.userDetails.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private final VerificationStrategyFactory verificationStrategyFactory;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> upload(@AuthenticationPrincipal SecurityUser securityUser,
                                 @RequestPart ImageIdentificationDto imageIdentificationDto,
                                 @RequestPart List<MultipartFile> images) {
        VerificationStrategy strategy = verificationStrategyFactory.getStrategy(imageIdentificationDto.getDomainType());
        imageService.upload(securityUser.getId(), strategy,
                ImageUploadDto.builder()
                        .domainId(imageIdentificationDto.getDomainId())
                        .domainType(imageIdentificationDto.getDomainType())
                        .imageDto(new ImageDto(imageIdentificationDto.getUsageType(), images))
                        .build());
        return ApiResponse.success();
    }
}
