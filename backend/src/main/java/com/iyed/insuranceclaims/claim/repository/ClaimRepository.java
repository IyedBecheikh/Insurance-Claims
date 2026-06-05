package com.iyed.insuranceclaims.claim.repository;

import com.iyed.insuranceclaims.claim.entity.Claim;
import com.iyed.insuranceclaims.claim.entity.ClaimStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, UUID>, JpaSpecificationExecutor<Claim> {

    Optional<Claim> findByClaimNumber(String claimNumber);

    List<Claim> findByClientId(UUID clientId);

    Optional<Claim> findByIdAndClientId(UUID id, UUID clientId);
}
