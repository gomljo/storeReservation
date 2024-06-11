package com.store.reservation.image.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.store.reservation.image.model.DomainType;
import com.store.reservation.image.util.exception.ImageClientRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.store.reservation.image.util.exception.ImageClientErrorCode.CAN_NOT_GET_IMAGE_DATA;

@Component
@RequiredArgsConstructor
public class CloudImageClient implements ImageClient {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    @Override
    public String upload(DomainType domainType, Long id, MultipartFile multipartFile) {
        try {
            List<String> directories = List.of(domainType.name(), String.valueOf(id));
            S3Identifier s3Identifier = new S3Identifier(directories, multipartFile.getOriginalFilename());
            String identifier = s3Identifier.getUrl();
            System.out.println(identifier);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(multipartFile.getSize());
            metadata.setContentType(multipartFile.getContentType());
            System.out.println("uploading...");
            this.amazonS3Client.putObject(this.bucket, identifier, multipartFile.getInputStream(), metadata);
            System.out.println("uploading finished");
            return this.amazonS3Client.getUrl(this.bucket, identifier).toString();
        } catch (IOException ioException) {
            throw new ImageClientRuntimeException(CAN_NOT_GET_IMAGE_DATA);
        }
    }

    @Override
    public void delete(String identifier) {
        this.amazonS3Client.deleteObject(this.bucket, identifier);
    }

    @Override
    public List<String> changeFileName(List<String> storedUrls) {
        List<String> urls = new ArrayList<>();
        for (String storedUrl : storedUrls) {
            String searchPattern = ".com/";
            int cuttingPoint = storedUrl.indexOf(searchPattern) + searchPattern.length();
            String fileName = storedUrl.substring(cuttingPoint);

            String[] elements = fileName.split("/");
            List<String> directory = new ArrayList<>(Arrays.asList(elements).subList(0, elements.length - 1));
            S3Identifier s3Identifier = new S3Identifier(directory, elements[elements.length - 1]);
            String identifier = s3Identifier.getUrl();
            urls.add(identifier);
        }
        return urls;
    }
}
