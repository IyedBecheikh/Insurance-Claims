package com.iyed.insuranceclaims.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record CreateClientRequestDto(
        @NotNull UUID userId,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        String address,
        @NotBlank String nationalId,
        @NotNull LocalDate dateOfBirth) {
}
