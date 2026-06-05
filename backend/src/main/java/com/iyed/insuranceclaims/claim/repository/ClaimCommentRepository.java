package com.iyed.insuranceclaims.claim.repository;

import com.iyed.insuranceclaims.claim.entity.ClaimComment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimCommentRepository extends JpaRepository<ClaimComment, UUID> {

    List<ClaimComment> findByClaimId(UUID claimId);
}
