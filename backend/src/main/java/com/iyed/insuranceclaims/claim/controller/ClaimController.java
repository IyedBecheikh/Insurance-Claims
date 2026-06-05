package com.iyed.insuranceclaims.claim.controller;

import com.iyed.insuranceclaims.auth.security.CustomUserPrincipal;
import com.iyed.insuranceclaims.claim.dto.ClaimResponseDto;
import com.iyed.insuranceclaims.claim.dto.CreateClaimRequestDto;
import com.iyed.insuranceclaims.claim.entity.ClaimStatus;
import com.iyed.insuranceclaims.claim.service.ClaimService;
import com.iyed.insuranceclaims.document.dto.AddClaimDocumentRequestDto;
import com.iyed.insuranceclaims.document.dto.ClaimDocumentResponseDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/claims")
@SecurityRequirement(name = "bearerAuth")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Create a draft claim")
    public ClaimResponseDto create(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody CreateClaimRequestDto request) {
        return claimService.createClaim(principal.getId(), request);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "List my claims")
    public List<ClaimResponseDto> findOwnClaims(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return claimService.findOwnClaims(principal.getId());
    }

    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Get my claim")
    public ClaimResponseDto findOwnClaim(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id) {
        return claimService.findOwnClaim(principal.getId(), id);
    }

    @PostMapping("/{id}/documents")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Add document metadata to a draft claim")
    public ClaimDocumentResponseDto addDocument(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id,
            @Valid @RequestBody AddClaimDocumentRequestDto request) {
        return claimService.addDocument(principal.getId(), id, request);
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('CLIENT')")
    @Operation(summary = "Submit a draft claim")
    public ClaimResponseDto submit(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id) {
        return claimService.submitClaim(principal.getId(), id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @Operation(summary = "List claims for reviewers")
    public List<ClaimResponseDto> findReviewerClaims(
            @RequestParam(required = false) ClaimStatus status,
            @RequestParam(required = false) UUID clientId,
            @RequestParam(required = false) String claimNumber) {
        return claimService.findReviewerClaims(status, clientId, claimNumber);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @Operation(summary = "Get claim by id for reviewers")
    public ClaimResponseDto findReviewerClaim(@PathVariable UUID id) {
        return claimService.findReviewerClaim(id);
    }

    @PatchMapping("/{id}/start-review")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @Operation(summary = "Move submitted claim to under review")
    public ClaimResponseDto startReview(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id) {
        return claimService.startReview(principal.getId(), id);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @Operation(summary = "Approve a claim under review")
    public ClaimResponseDto approve(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id) {
        return claimService.approve(principal.getId(), id);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @Operation(summary = "Reject a claim under review")
    public ClaimResponseDto reject(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id) {
        return claimService.reject(principal.getId(), id);
    }

    @PatchMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    @Operation(summary = "Mark an approved claim as paid")
    public ClaimResponseDto pay(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable UUID id) {
        return claimService.pay(principal.getId(), id);
    }
}
