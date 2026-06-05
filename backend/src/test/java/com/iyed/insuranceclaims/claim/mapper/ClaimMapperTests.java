package com.iyed.insuranceclaims.claim.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.iyed.insuranceclaims.claim.dto.ClaimResponseDto;
import com.iyed.insuranceclaims.claim.entity.Claim;
import com.iyed.insuranceclaims.claim.entity.ClaimComment;
import com.iyed.insuranceclaims.claim.entity.ClaimStatus;
import com.iyed.insuranceclaims.client.entity.Client;
import com.iyed.insuranceclaims.contract.entity.Contract;
import com.iyed.insuranceclaims.contract.entity.ContractStatus;
import com.iyed.insuranceclaims.contract.entity.ContractType;
import com.iyed.insuranceclaims.document.entity.ClaimDocument;
import com.iyed.insuranceclaims.document.mapper.ClaimDocumentMapper;
import com.iyed.insuranceclaims.user.entity.Role;
import com.iyed.insuranceclaims.user.entity.User;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(ClaimMapperTests.MapperTestConfiguration.class)
class ClaimMapperTests {

    @Autowired
    private ClaimMapper claimMapper;

    @Test
    void toResponseDtoMapsIdsAndNestedCollections() {
        User reviewer = new User();
        reviewer.setId(UUID.randomUUID());
        reviewer.setEmail("agent@example.com");
        reviewer.setPasswordHash("hashed");
        reviewer.setRole(Role.AGENT);
        reviewer.setEnabled(true);
        reviewer.setCreatedAt(LocalDateTime.now());

        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setFirstName("Iyed");
        client.setLastName("Becheikh");
        client.setPhone("+21600000000");
        client.setNationalId("AA123456");
        client.setDateOfBirth(LocalDate.of(1990, 1, 1));
        client.setRegistrationDate(LocalDateTime.now());
        client.setUser(reviewer);

        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setClient(client);
        contract.setContractNumber("CN-2026-0001");
        contract.setType(ContractType.HEALTH);
        contract.setStartDate(LocalDate.of(2026, 1, 1));
        contract.setEndDate(LocalDate.of(2026, 12, 31));
        contract.setCoverageLimit(new BigDecimal("5000.00"));
        contract.setReimbursementRate(new BigDecimal("0.80"));
        contract.setStatus(ContractStatus.ACTIVE);

        Claim claim = new Claim();
        claim.setId(UUID.randomUUID());
        claim.setClient(client);
        claim.setContract(contract);
        claim.setClaimNumber("CLM-2026-0001");
        claim.setClaimAmount(new BigDecimal("450.00"));
        claim.setReimbursementAmount(new BigDecimal("360.00"));
        claim.setStatus(ClaimStatus.UNDER_REVIEW);
        claim.setDescription("Consultation");
        claim.setMedicalServiceDate(LocalDate.of(2026, 5, 20));
        claim.setSubmittedAt(LocalDateTime.of(2026, 5, 21, 10, 0));
        claim.setReviewedAt(LocalDateTime.of(2026, 5, 22, 12, 30));
        claim.setReviewedBy(reviewer);

        ClaimDocument document = new ClaimDocument();
        document.setId(UUID.randomUUID());
        document.setClaim(claim);
        document.setFileName("invoice.pdf");
        document.setFileType("application/pdf");
        document.setFilePath("uploads/invoice.pdf");
        document.setFileSize(2048L);
        document.setUploadedAt(LocalDateTime.of(2026, 5, 21, 10, 5));

        ClaimComment comment = new ClaimComment();
        comment.setId(UUID.randomUUID());
        comment.setClaim(claim);
        comment.setAuthor(reviewer);
        comment.setComment("Looks valid");
        comment.setCreatedAt(LocalDateTime.of(2026, 5, 22, 12, 35));

        claim.setDocuments(List.of(document));
        claim.setComments(List.of(comment));

        ClaimResponseDto dto = claimMapper.toResponseDto(claim);

        assertThat(dto.id()).isEqualTo(claim.getId());
        assertThat(dto.clientId()).isEqualTo(client.getId());
        assertThat(dto.contractId()).isEqualTo(contract.getId());
        assertThat(dto.reviewedBy()).isEqualTo(reviewer.getId());
        assertThat(dto.documents()).singleElement().satisfies(mappedDocument -> {
            assertThat(mappedDocument.id()).isEqualTo(document.getId());
            assertThat(mappedDocument.claimId()).isEqualTo(claim.getId());
        });
        assertThat(dto.comments()).singleElement().satisfies(mappedComment -> {
            assertThat(mappedComment.id()).isEqualTo(comment.getId());
            assertThat(mappedComment.authorId()).isEqualTo(reviewer.getId());
        });
    }

    @Configuration
    @ComponentScan(basePackageClasses = {ClaimMapper.class, ClaimCommentMapper.class, ClaimDocumentMapper.class})
    static class MapperTestConfiguration {
    }
}
