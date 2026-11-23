
package com.messboard.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.io.IOException;
import java.util.UUID;
import java.util.Set;

@Service
public class FileStorageService {

    private final Path uploadPath = Paths.get("uploads")
            .toAbsolutePath().normalize();

    public FileStorageService() throws IOException {
        Files.createDirectories(uploadPath);
    }

    public String save(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image is required");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Image exceeds 5 MB");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
            !Set.of("image/jpeg", "image/png", "image/webp")
                .contains(contentType)) {
            throw new IllegalArgumentException("Invalid image type");
        }

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException("Invalid image");
        };

        String filename = UUID.randomUUID() + extension;

        Path target = uploadPath.resolve(filename).normalize();

        if (!target.getParent().equals(uploadPath)) {
            throw new IOException("Invalid file path");
        }

        Files.copy(
            file.getInputStream(),
            target,
            StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/" + filename;
    }
}
