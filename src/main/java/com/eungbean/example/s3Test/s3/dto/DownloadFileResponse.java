package com.eungbean.example.s3Test.s3.dto;

import lombok.Builder;

@Builder
public record DownloadFileResponse(
        String key
) {
}
