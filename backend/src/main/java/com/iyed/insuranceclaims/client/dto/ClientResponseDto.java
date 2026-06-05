package com.iyed.insuranceclaims.client.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClientResponseDto(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String phone,
        String address,
        String nationalId,
        LocalDate dateOfBirth,
        LocalDateTime registrationDate) {
}
