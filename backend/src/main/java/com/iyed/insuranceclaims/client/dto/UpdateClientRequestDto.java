package com.iyed.insuranceclaims.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateClientRequestDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String phone,
        String address,
        @NotBlank String nationalId,
        @NotNull LocalDate dateOfBirth) {
}
