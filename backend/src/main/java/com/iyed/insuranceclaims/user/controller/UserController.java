package com.iyed.insuranceclaims.user.controller;

import com.iyed.insuranceclaims.user.dto.CreateUserRequestDto;
import com.iyed.insuranceclaims.user.dto.UserResponseDto;
import com.iyed.insuranceclaims.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user", description = "Admin-only user creation endpoint")
    public UserResponseDto create(@Valid @RequestBody CreateUserRequestDto request) {
        return userService.create(request);
    }

    @GetMapping
    @Operation(summary = "List users")
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserResponseDto findById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @PatchMapping("/{id}/enabled")
    @Operation(summary = "Enable or disable a user")
    public UserResponseDto updateEnabled(@PathVariable UUID id, @RequestParam boolean enabled) {
        return userService.updateEnabled(id, enabled);
    }
}
