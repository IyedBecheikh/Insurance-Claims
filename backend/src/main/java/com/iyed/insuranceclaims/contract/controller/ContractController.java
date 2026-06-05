package com.iyed.insuranceclaims.contract.controller;

import com.iyed.insuranceclaims.auth.security.CustomUserPrincipal;
import com.iyed.insuranceclaims.contract.dto.ContractResponseDto;
import com.iyed.insuranceclaims.contract.dto.CreateContractRequestDto;
import com.iyed.insuranceclaims.contract.dto.UpdateContractRequestDto;
import com.iyed.insuranceclaims.contract.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contracts")
@SecurityRequirement(name = "bearerAuth")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a contract")
    public ContractResponseDto create(@Valid @RequestBody CreateContractRequestDto request) {
        return contractService.create(request);
    }

    @GetMapping
    @Operation(summary = "List contracts")
    public List<ContractResponseDto> findAll() {
        return contractService.findAll();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "List contracts for the authenticated client")
    public List<ContractResponseDto> findOwnContracts(
            @AuthenticationPrincipal CustomUserPrincipal principal) {
        return contractService.findOwnContracts(principal.getId());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contract by id")
    public ContractResponseDto findById(@PathVariable UUID id) {
        return contractService.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a contract")
    public ContractResponseDto update(@PathVariable UUID id, @Valid @RequestBody UpdateContractRequestDto request) {
        return contractService.update(id, request);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a contract")
    public ContractResponseDto activate(@PathVariable UUID id) {
        return contractService.activate(id);
    }

    @PatchMapping("/{id}/suspend")
    @Operation(summary = "Suspend a contract")
    public ContractResponseDto suspend(@PathVariable UUID id) {
        return contractService.suspend(id);
    }
}
