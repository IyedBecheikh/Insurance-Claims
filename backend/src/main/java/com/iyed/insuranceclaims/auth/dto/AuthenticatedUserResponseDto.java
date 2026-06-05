package com.iyed.insuranceclaims.auth.dto;

import com.iyed.insuranceclaims.user.entity.Role;
import java.util.UUID;

public record AuthenticatedUserResponseDto(
        UUID id,
        String email,
        Role role) {
}
