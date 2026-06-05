package com.iyed.insuranceclaims.document.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClaimDocumentResponseDto(
        UUID id,
        UUID claimId,
        String fileName,
        String fileType,
        String filePath,
        long fileSize,
        LocalDateTime uploadedAt) {
}
