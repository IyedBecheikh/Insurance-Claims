package com.iyed.insuranceclaims.contract.dto;

import com.iyed.insuranceclaims.contract.entity.ContractType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateContractRequestDto(
        @NotNull UUID clientId,
        @NotBlank String contractNumber,
        @NotNull ContractType type,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @NotNull @DecimalMin("0.01") BigDecimal coverageLimit,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal reimbursementRate) {
}
