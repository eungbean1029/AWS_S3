package com.eungbean.example.s3Test.s3.dto;

import jakarta.validation.constraints.NotNull;

public record DownloadFileRequest(
        @NotNull String key
) {
}
