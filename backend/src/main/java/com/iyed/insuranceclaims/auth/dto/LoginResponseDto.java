package com.iyed.insuranceclaims.auth.dto;

import com.iyed.insuranceclaims.user.entity.Role;

public record LoginResponseDto(
        String accessToken,
        String tokenType,
        Role role,
        long expiresIn) {
}
