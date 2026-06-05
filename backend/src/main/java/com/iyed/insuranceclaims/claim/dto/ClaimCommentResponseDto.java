package com.iyed.insuranceclaims.claim.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ClaimCommentResponseDto(
        UUID id,
        UUID claimId,
        UUID authorId,
        String comment,
        LocalDateTime createdAt) {
}
