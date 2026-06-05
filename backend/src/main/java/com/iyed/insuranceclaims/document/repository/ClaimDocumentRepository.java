package com.iyed.insuranceclaims.document.repository;

import com.iyed.insuranceclaims.document.entity.ClaimDocument;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimDocumentRepository extends JpaRepository<ClaimDocument, UUID> {

    List<ClaimDocument> findByClaimId(UUID claimId);
}
