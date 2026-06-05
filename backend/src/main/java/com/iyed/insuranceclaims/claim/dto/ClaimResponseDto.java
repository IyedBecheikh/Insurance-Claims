package com.iyed.insuranceclaims.claim.dto;

import com.iyed.insuranceclaims.claim.entity.ClaimStatus;
import com.iyed.insuranceclaims.document.dto.ClaimDocumentResponseDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ClaimResponseDto(
        UUID id,
        UUID clientId,
        UUID contractId,
        String claimNumber,
        BigDecimal claimAmount,
        BigDecimal reimbursementAmount,
        ClaimStatus status,
        String description,
        LocalDate medicalServiceDate,
        LocalDateTime submittedAt,
        LocalDateTime reviewedAt,
        UUID reviewedBy,
        List<ClaimDocumentResponseDto> documents,
        List<ClaimCommentResponseDto> comments) {
}
