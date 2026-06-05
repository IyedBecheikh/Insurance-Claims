package com.iyed.insuranceclaims.document.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AddClaimDocumentRequestDto(
        @NotBlank String fileName,
        @NotBlank String fileType,
        @NotBlank String filePath,
        @Min(1) long fileSize) {
}
