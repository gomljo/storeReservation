package com.store.reservation.image.dto;

import com.store.reservation.image.model.UsageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private UsageType usageType;
    private List<MultipartFile> images;
}
