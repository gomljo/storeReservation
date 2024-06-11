package com.store.reservation.image.util;

import com.store.reservation.image.util.exception.ImageClientRuntimeException;

import java.util.List;
import java.util.UUID;

import static com.store.reservation.image.util.exception.ImageClientErrorCode.INVALID_EXTENSION;
import static com.store.reservation.image.util.exception.ImageClientErrorCode.INVALID_SYMBOL;

public class S3Identifier {
    private static final String LEVEL_DELIMITER = "/";
    private static final String UNDER_BAR = "_";
    private static final String DOT = ".";
    private static final String DOT_DELIMITER = "\\.";
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "webp", "png", "jpeg");
    private static final List<String> NOT_ALLOWED_SYMBOL = List.of("_", "*", "!", "=", "&", "^", ".", "/");
    private static final String SEARCH_PATTERN = ".com/";
    private final List<String> directories;
    private final String fileName;

    public S3Identifier(List<String> directories, String fileName) {
        this.directories = directories;
        this.fileName = fileName;
    }

    private String makeFolderPath() {
        return String.join(LEVEL_DELIMITER, this.directories) + LEVEL_DELIMITER;
    }

    private String makeFileName() {
        String[] fileAndExtension = this.fileName.split(DOT_DELIMITER);
        if (isInvalidExtension(fileAndExtension[1])) {
            throw new ImageClientRuntimeException(INVALID_EXTENSION);
        }
        if (existsNotAllowedSymbolInFileName(fileAndExtension[0])) {
            throw new ImageClientRuntimeException(INVALID_SYMBOL);
        }
        return fileAndExtension[0] + UNDER_BAR + UUID.randomUUID() + DOT + fileAndExtension[1];
    }

    private boolean isInvalidExtension(String extension) {
        return !ALLOWED_EXTENSIONS.contains(extension);
    }

    private boolean existsNotAllowedSymbolInFileName(String fileName) {
        return NOT_ALLOWED_SYMBOL.stream().anyMatch(fileName::contains);
    }

    public String getUrl() {
        String folderPath = this.makeFolderPath();
        String fileName = this.makeFileName();
        return folderPath + fileName;
    }

}
