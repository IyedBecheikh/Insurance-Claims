package com.iyed.insuranceclaims.user.dto;

import com.iyed.insuranceclaims.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull Role role,
        boolean enabled) {
}
