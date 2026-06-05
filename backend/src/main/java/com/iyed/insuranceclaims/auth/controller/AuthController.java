package com.iyed.insuranceclaims.auth.controller;

import com.iyed.insuranceclaims.auth.dto.AuthenticatedUserResponseDto;
import com.iyed.insuranceclaims.auth.dto.LoginRequestDto;
import com.iyed.insuranceclaims.auth.dto.LoginResponseDto;
import com.iyed.insuranceclaims.auth.security.CustomUserPrincipal;
import com.iyed.insuranceclaims.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthenticatedUserResponseDto> me(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        return ResponseEntity.ok(authService.getAuthenticatedUser(principal));
    }

    @GetMapping("/admin-only")
    public ResponseEntity<AuthenticatedUserResponseDto> adminOnly(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        return ResponseEntity.ok(authService.getAuthenticatedUser(principal));
    }

    @GetMapping("/agent-only")
    public ResponseEntity<AuthenticatedUserResponseDto> agentOnly(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        return ResponseEntity.ok(authService.getAuthenticatedUser(principal));
    }

    @GetMapping("/client-only")
    public ResponseEntity<AuthenticatedUserResponseDto> clientOnly(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        return ResponseEntity.ok(authService.getAuthenticatedUser(principal));
    }
}
