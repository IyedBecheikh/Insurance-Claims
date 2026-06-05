package com.iyed.insuranceclaims.user.dto;

import com.iyed.insuranceclaims.user.entity.Role;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String email,
        Role role,
        boolean enabled,
        LocalDateTime createdAt) {
}
