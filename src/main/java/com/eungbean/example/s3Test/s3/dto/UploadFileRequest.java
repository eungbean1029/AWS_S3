package com.eungbean.example.s3Test.s3.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadFileRequest(
        String fileId,
        @NotNull MultipartFile file
) {
}
