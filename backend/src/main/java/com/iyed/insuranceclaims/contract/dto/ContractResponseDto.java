package com.iyed.insuranceclaims.contract.dto;

import com.iyed.insuranceclaims.contract.entity.ContractStatus;
import com.iyed.insuranceclaims.contract.entity.ContractType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ContractResponseDto(
        UUID id,
        UUID clientId,
        String contractNumber,
        ContractType type,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal coverageLimit,
        BigDecimal reimbursementRate,
        ContractStatus status) {
}
