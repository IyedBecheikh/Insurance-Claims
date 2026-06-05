package com.iyed.insuranceclaims.auth.service;

import com.iyed.insuranceclaims.auth.dto.AuthenticatedUserResponseDto;
import com.iyed.insuranceclaims.auth.dto.LoginRequestDto;
import com.iyed.insuranceclaims.auth.dto.LoginResponseDto;
import com.iyed.insuranceclaims.auth.security.CustomUserPrincipal;
import com.iyed.insuranceclaims.auth.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);

        return new LoginResponseDto(
                token,
                "Bearer",
                principal.getRole(),
                jwtService.getExpirationSeconds());
    }

    public AuthenticatedUserResponseDto getAuthenticatedUser(CustomUserPrincipal principal) {
        return new AuthenticatedUserResponseDto(principal.getId(), principal.getUsername(), principal.getRole());
    }
}
