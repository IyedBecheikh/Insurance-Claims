package com.iyed.insuranceclaims.claim.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateClaimRequestDto(
        @NotNull UUID contractId,
        @NotNull @DecimalMin("0.01") BigDecimal claimAmount,
        String description,
        @NotNull LocalDate medicalServiceDate) {
}
