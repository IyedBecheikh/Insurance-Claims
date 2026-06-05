package com.iyed.insuranceclaims.claim;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class ClaimWorkflowIntegrationTests {

    private static final UUID SEEDED_CLIENT_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpMockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    void clientCanCreateDraftAddDocumentSubmitAndViewOwnClaim() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");
        String clientToken = loginAndExtractToken("client@insurance.local", "Password123!");
        UUID contractId = createContract(adminToken, SEEDED_CLIENT_ID, "CT-PH4-001", 1200.00, 0.80);

        UUID claimId = createClaim(clientToken, contractId, 500.00, "2026-03-10");

        mockMvc.perform(get("/api/claims/my")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id=='%s')]".formatted(claimId)).isNotEmpty())
                .andExpect(jsonPath("$[?(@.id=='%s')].status".formatted(claimId)).value("DRAFT"));

        mockMvc.perform(post("/api/claims/{id}/documents", claimId)
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fileName": "invoice.pdf",
                                  "fileType": "application/pdf",
                                  "filePath": "/claims/invoice.pdf",
                                  "fileSize": 24576
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fileName").value("invoice.pdf"));

        mockMvc.perform(post("/api/claims/{id}/submit", claimId)
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUBMITTED"))
                .andExpect(jsonPath("$.documents[0].fileName").value("invoice.pdf"));

        mockMvc.perform(get("/api/claims/my/{id}", claimId)
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(claimId.toString()))
                .andExpect(jsonPath("$.status").value("SUBMITTED"));
    }

    @Test
    void clientCannotSubmitDraftWithoutDocuments() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");
        String clientToken = loginAndExtractToken("client@insurance.local", "Password123!");
        UUID contractId = createContract(adminToken, SEEDED_CLIENT_ID, "CT-PH4-002", 1000.00, 0.70);
        UUID claimId = createClaim(clientToken, contractId, 250.00, "2026-04-05");

        mockMvc.perform(post("/api/claims/{id}/submit", claimId)
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void agentCanReviewApproveAndPayClaimWithReimbursementFormulaApplied() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");
        String clientToken = loginAndExtractToken("client@insurance.local", "Password123!");
        String agentToken = loginAndExtractToken("agent@insurance.local", "Password123!");
        UUID contractId = createContract(adminToken, SEEDED_CLIENT_ID, "CT-PH4-003", 1000.00, 0.80);
        UUID claimId = createSubmittedClaim(clientToken, contractId, 500.00, "2026-03-20");

        mockMvc.perform(patch("/api/claims/{id}/start-review", claimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UNDER_REVIEW"));

        mockMvc.perform(patch("/api/claims/{id}/approve", claimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.reimbursementAmount").value(400.00));

        mockMvc.perform(patch("/api/claims/{id}/pay", claimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"))
                .andExpect(jsonPath("$.reimbursementAmount").value(400.00));
    }

    @Test
    void reimbursementIsCappedByRemainingCoverage() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");
        String clientToken = loginAndExtractToken("client@insurance.local", "Password123!");
        String agentToken = loginAndExtractToken("agent@insurance.local", "Password123!");
        UUID contractId = createContract(adminToken, SEEDED_CLIENT_ID, "CT-PH4-004", 450.00, 0.80);

        UUID firstClaimId = createSubmittedClaim(clientToken, contractId, 500.00, "2026-05-01");
        reviewApproveAndPay(agentToken, firstClaimId);

        UUID secondClaimId = createSubmittedClaim(clientToken, contractId, 500.00, "2026-05-10");
        mockMvc.perform(patch("/api/claims/{id}/start-review", secondClaimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/claims/{id}/approve", secondClaimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reimbursementAmount").value(50.00));
    }

    @Test
    void reviewerCanFilterClaimsByStatus() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");
        String clientToken = loginAndExtractToken("client@insurance.local", "Password123!");
        String agentToken = loginAndExtractToken("agent@insurance.local", "Password123!");
        UUID contractId = createContract(adminToken, SEEDED_CLIENT_ID, "CT-PH4-005", 1000.00, 0.80);
        UUID claimId = createSubmittedClaim(clientToken, contractId, 200.00, "2026-02-15");

        mockMvc.perform(get("/api/claims")
                        .header("Authorization", "Bearer " + agentToken)
                        .param("status", "SUBMITTED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id=='%s')]".formatted(claimId)).isNotEmpty());
    }

    private void reviewApproveAndPay(String agentToken, UUID claimId) throws Exception {
        mockMvc.perform(patch("/api/claims/{id}/start-review", claimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/api/claims/{id}/approve", claimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk());
        mockMvc.perform(patch("/api/claims/{id}/pay", claimId)
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isOk());
    }

    private UUID createSubmittedClaim(String clientToken, UUID contractId, double amount, String serviceDate) throws Exception {
        UUID claimId = createClaim(clientToken, contractId, amount, serviceDate);
        mockMvc.perform(post("/api/claims/{id}/documents", claimId)
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fileName": "support.pdf",
                                  "fileType": "application/pdf",
                                  "filePath": "/claims/support.pdf",
                                  "fileSize": 1024
                                }
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/claims/{id}/submit", claimId)
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk());
        return claimId;
    }

    private UUID createClaim(String clientToken, UUID contractId, double amount, String serviceDate) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/claims")
                        .header("Authorization", "Bearer " + clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "contractId": "%s",
                                  "claimAmount": %.2f,
                                  "description": "Consultation",
                                  "medicalServiceDate": "%s"
                                }
                                """.formatted(contractId, amount, serviceDate)))
                .andExpect(status().isCreated())
                .andReturn();

        return UUID.fromString(objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText());
    }

    private UUID createContract(String adminToken, UUID clientId, String contractNumber, double coverageLimit, double rate)
            throws Exception {
        MvcResult result = mockMvc.perform(post("/api/contracts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId": "%s",
                                  "contractNumber": "%s",
                                  "type": "HEALTH",
                                  "startDate": "2026-01-01",
                                  "endDate": "2026-12-31",
                                  "coverageLimit": %.2f,
                                  "reimbursementRate": %.2f,
                                  "status": "ACTIVE"
                                }
                                """.formatted(clientId, contractNumber, coverageLimit, rate)))
                .andExpect(status().isCreated())
                .andReturn();

        return UUID.fromString(objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText());
    }

    private String loginAndExtractToken(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.get("accessToken").asText();
    }
}
