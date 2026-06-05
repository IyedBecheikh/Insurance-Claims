package com.iyed.insuranceclaims.claim.service;

import com.iyed.insuranceclaims.claim.dto.ClaimResponseDto;
import com.iyed.insuranceclaims.claim.dto.CreateClaimRequestDto;
import com.iyed.insuranceclaims.claim.entity.Claim;
import com.iyed.insuranceclaims.claim.entity.ClaimStatus;
import com.iyed.insuranceclaims.claim.mapper.ClaimMapper;
import com.iyed.insuranceclaims.claim.repository.ClaimRepository;
import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.client.repository.ClientRepository;
import com.iyed.insuranceclaims.common.exception.ResourceNotFoundException;
import com.iyed.insuranceclaims.contract.entity.Contract;
import com.iyed.insuranceclaims.contract.entity.ContractStatus;
import com.iyed.insuranceclaims.contract.repository.ContractRepository;
import com.iyed.insuranceclaims.document.dto.AddClaimDocumentRequestDto;
import com.iyed.insuranceclaims.document.dto.ClaimDocumentResponseDto;
import com.iyed.insuranceclaims.document.entity.ClaimDocument;
import com.iyed.insuranceclaims.document.mapper.ClaimDocumentMapper;
import com.iyed.insuranceclaims.document.repository.ClaimDocumentRepository;
import com.iyed.insuranceclaims.user.entity.User;
import com.iyed.insuranceclaims.user.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final ClaimDocumentRepository claimDocumentRepository;
    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClaimMapper claimMapper;
    private final ClaimDocumentMapper claimDocumentMapper;

    public ClaimService(
            ClaimRepository claimRepository,
            ClaimDocumentRepository claimDocumentRepository,
            ContractRepository contractRepository,
            ClientRepository clientRepository,
            UserRepository userRepository,
            ClaimMapper claimMapper,
            ClaimDocumentMapper claimDocumentMapper) {
        this.claimRepository = claimRepository;
        this.claimDocumentRepository = claimDocumentRepository;
        this.contractRepository = contractRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.claimMapper = claimMapper;
        this.claimDocumentMapper = claimDocumentMapper;
    }

    public ClaimResponseDto createClaim(UUID userId, CreateClaimRequestDto request) {
        Client client = getClientForUser(userId);
        Contract contract = contractRepository.findById(request.contractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract not found"));

        validateClientContract(client, contract);
        validateContractEligibility(contract, request.medicalServiceDate());

        Claim claim = new Claim();
        claim.setId(UUID.randomUUID());
        claim.setClient(client);
        claim.setContract(contract);
        claim.setClaimNumber("CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        claim.setClaimAmount(request.claimAmount().setScale(2, RoundingMode.HALF_UP));
        claim.setDescription(request.description());
        claim.setMedicalServiceDate(request.medicalServiceDate());
        claim.setStatus(ClaimStatus.DRAFT);

        return claimMapper.toResponseDto(claimRepository.save(claim));
    }

    @Transactional(readOnly = true)
    public List<ClaimResponseDto> findOwnClaims(UUID userId) {
        Client client = getClientForUser(userId);
        return claimRepository.findByClientId(client.getId()).stream()
                .map(claimMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClaimResponseDto findOwnClaim(UUID userId, UUID claimId) {
        Client client = getClientForUser(userId);
        Claim claim = claimRepository.findByIdAndClientId(claimId, client.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        return claimMapper.toResponseDto(claim);
    }

    public ClaimDocumentResponseDto addDocument(UUID userId, UUID claimId, AddClaimDocumentRequestDto request) {
        Client client = getClientForUser(userId);
        Claim claim = claimRepository.findByIdAndClientId(claimId, client.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        ensureStatus(claim, ClaimStatus.DRAFT, "Documents can only be added to draft claims");

        ClaimDocument document = new ClaimDocument();
        document.setId(UUID.randomUUID());
        document.setClaim(claim);
        document.setFileName(request.fileName());
        document.setFileType(request.fileType());
        document.setFilePath(request.filePath());
        document.setFileSize(request.fileSize());
        document.setUploadedAt(LocalDateTime.now());

        return claimDocumentMapper.toResponseDto(claimDocumentRepository.save(document));
    }

    public ClaimResponseDto submitClaim(UUID userId, UUID claimId) {
        Client client = getClientForUser(userId);
        Claim claim = claimRepository.findByIdAndClientId(claimId, client.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
        ensureStatus(claim, ClaimStatus.DRAFT, "Only draft claims can be submitted");
        if (claim.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("A claim must have at least one document before submission");
        }

        claim.setStatus(ClaimStatus.SUBMITTED);
        claim.setSubmittedAt(LocalDateTime.now());
        return claimMapper.toResponseDto(claimRepository.save(claim));
    }

    @Transactional(readOnly = true)
    public List<ClaimResponseDto> findReviewerClaims(ClaimStatus status, UUID clientId, String claimNumber) {
        Specification<Claim> specification = (root, query, cb) -> cb.conjunction();
        if (status != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        if (clientId != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("client").get("id"), clientId));
        }
        if (claimNumber != null && !claimNumber.isBlank()) {
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("claimNumber")), "%" + claimNumber.toLowerCase() + "%"));
        }
        return claimRepository.findAll(specification).stream().map(claimMapper::toResponseDto).toList();
    }

    @Transactional(readOnly = true)
    public ClaimResponseDto findReviewerClaim(UUID claimId) {
        return claimMapper.toResponseDto(getClaim(claimId));
    }

    public ClaimResponseDto startReview(UUID reviewerUserId, UUID claimId) {
        Claim claim = getClaim(claimId);
        ensureStatus(claim, ClaimStatus.SUBMITTED, "Only submitted claims can move to under review");
        claim.setStatus(ClaimStatus.UNDER_REVIEW);
        claim.setReviewedAt(LocalDateTime.now());
        claim.setReviewedBy(getReviewer(reviewerUserId));
        return claimMapper.toResponseDto(claimRepository.save(claim));
    }

    public ClaimResponseDto approve(UUID reviewerUserId, UUID claimId) {
        Claim claim = getClaim(claimId);
        ensureStatus(claim, ClaimStatus.UNDER_REVIEW, "Only claims under review can be approved");
        claim.setStatus(ClaimStatus.APPROVED);
        claim.setReviewedAt(LocalDateTime.now());
        claim.setReviewedBy(getReviewer(reviewerUserId));
        claim.setReimbursementAmount(calculateReimbursement(claim));
        return claimMapper.toResponseDto(claimRepository.save(claim));
    }

    public ClaimResponseDto reject(UUID reviewerUserId, UUID claimId) {
        Claim claim = getClaim(claimId);
        ensureStatus(claim, ClaimStatus.UNDER_REVIEW, "Only claims under review can be rejected");
        claim.setStatus(ClaimStatus.REJECTED);
        claim.setReviewedAt(LocalDateTime.now());
        claim.setReviewedBy(getReviewer(reviewerUserId));
        claim.setReimbursementAmount(null);
        return claimMapper.toResponseDto(claimRepository.save(claim));
    }

    public ClaimResponseDto pay(UUID reviewerUserId, UUID claimId) {
        Claim claim = getClaim(claimId);
        ensureStatus(claim, ClaimStatus.APPROVED, "Only approved claims can be marked as paid");
        claim.setStatus(ClaimStatus.PAID);
        claim.setReviewedAt(LocalDateTime.now());
        claim.setReviewedBy(getReviewer(reviewerUserId));
        claim.setReimbursementAmount(calculateReimbursement(claim));
        return claimMapper.toResponseDto(claimRepository.save(claim));
    }

    private Claim getClaim(UUID claimId) {
        return claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));
    }

    private Client getClientForUser(UUID userId) {
        return clientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client profile not found"));
    }

    private User getReviewer(UUID reviewerUserId) {
        return userRepository.findById(reviewerUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));
    }

    private void validateClientContract(Client client, Contract contract) {
        if (!contract.getClient().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Clients can only create claims for their own contracts");
        }
    }

    private void validateContractEligibility(Contract contract, java.time.LocalDate serviceDate) {
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new IllegalArgumentException("Claims can only be created for active contracts");
        }
        if (serviceDate.isBefore(contract.getStartDate()) || serviceDate.isAfter(contract.getEndDate())) {
            throw new IllegalArgumentException("Medical service date must fall within the contract date range");
        }
    }

    private void ensureStatus(Claim claim, ClaimStatus expectedStatus, String message) {
        if (claim.getStatus() != expectedStatus) {
            throw new IllegalStateException(message);
        }
    }

    private BigDecimal calculateReimbursement(Claim claim) {
        BigDecimal contractLimit = claim.getContract().getCoverageLimit();
        BigDecimal paidTotal = claim.getContract().getClaims().stream()
                .filter(existing -> existing.getStatus() == ClaimStatus.PAID)
                .filter(existing -> !existing.getId().equals(claim.getId()))
                .map(Claim::getReimbursementAmount)
                .filter(java.util.Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remainingCoverage = contractLimit.subtract(paidTotal);
        if (remainingCoverage.signum() < 0) {
            remainingCoverage = BigDecimal.ZERO;
        }

        BigDecimal calculated = claim.getClaimAmount()
                .multiply(claim.getContract().getReimbursementRate())
                .setScale(2, RoundingMode.HALF_UP);
        return calculated.min(remainingCoverage).setScale(2, RoundingMode.HALF_UP);
    }
}
