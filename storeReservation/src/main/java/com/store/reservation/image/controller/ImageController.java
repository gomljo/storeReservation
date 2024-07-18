package com.store.reservation.image.controller;

import com.store.reservation.apiResponse.ApiResponse;
import com.store.reservation.image.dto.ImageDto;
import com.store.reservation.image.dto.ImageIdentificationDto;
import com.store.reservation.image.dto.ImageSearchResult;
import com.store.reservation.image.dto.ImageUploadDto;
import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.model.ImageEntity;
import com.store.reservation.image.service.ImageService;
import com.store.reservation.member.security.userDetails.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> upload(@AuthenticationPrincipal SecurityUser securityUser,
                                 @RequestPart ImageIdentificationDto imageIdentificationDto,
                                 @RequestPart List<MultipartFile> images) {
        imageService.upload(ImageUploadDto.builder()
                .userId(securityUser.getId())
                .domainId(imageIdentificationDto.getDomainId())
                .domainType(imageIdentificationDto.getDomainType())
                .imageDto(new ImageDto(imageIdentificationDto.getUsageType(), images))
                .build());
        return ApiResponse.success();
    }

    @GetMapping
    public ApiResponse<List<ImageSearchResult>> search(@AuthenticationPrincipal SecurityUser securityUser,
                                                 @RequestHeader DomainType domainType,
                                                 @RequestHeader Long domainId) {
        return ApiResponse.success(imageService.searchBy(securityUser.getId(), domainType, domainId)
                .stream().map(ImageSearchResult::from).collect(Collectors.toList()));
    }

    @PatchMapping("/{imageId}")
    public ApiResponse<Long> change(@AuthenticationPrincipal SecurityUser securityUser,
                                 @PathVariable Long imageId,
                                 @RequestParam MultipartFile updatedImage) {
        return ApiResponse.success(imageService.update(securityUser.getId(), imageId, updatedImage));
    }

    @DeleteMapping("/{imageId}")
    public ApiResponse<?> delete(@AuthenticationPrincipal SecurityUser securityUser,
                                 @PathVariable Long imageId) {
        imageService.delete(securityUser.getId(), imageId);
        return ApiResponse.success();
    }

}

