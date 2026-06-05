package com.iyed.insuranceclaims.admin;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
class AdminManagementIntegrationTests {

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
    void apiDocsEndpointIsExposed() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").isString())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/api/auth/login")));
    }

    @Test
    void adminCanCreateListGetAndDisableUsers() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");

        MvcResult createResult = mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "new.user@insurance.local",
                                  "password": "Password123!",
                                  "role": "CLIENT",
                                  "enabled": true
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("new.user@insurance.local"))
                .andExpect(jsonPath("$.role").value("CLIENT"))
                .andReturn();

        UUID userId = UUID.fromString(objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText());

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.email=='new.user@insurance.local')]").isNotEmpty());

        mockMvc.perform(get("/api/users/{id}", userId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.enabled").value(true));

        mockMvc.perform(patch("/api/users/{id}/enabled", userId)
                        .header("Authorization", "Bearer " + adminToken)
                        .param("enabled", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enabled").value(false));
    }

    @Test
    void nonAdminCannotAccessAdminManagementEndpoints() throws Exception {
        String agentToken = loginAndExtractToken("agent@insurance.local", "Password123!");

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + agentToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void adminCanCreateGetListAndUpdateClients() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");

        UUID userId = createUser(adminToken, "client.phase3@insurance.local", "CLIENT");

        MvcResult createResult = mockMvc.perform(post("/api/clients")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "firstName": "Phase",
                                  "lastName": "Three",
                                  "phone": "+21611112222",
                                  "address": "Tunis Centre",
                                  "nationalId": "CL-3001",
                                  "dateOfBirth": "1990-01-15"
                                }
                                """.formatted(userId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.firstName").value("Phase"))
                .andReturn();

        UUID clientId = UUID.fromString(objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText());

        mockMvc.perform(get("/api/clients")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id=='%s')]".formatted(clientId)).isNotEmpty());

        mockMvc.perform(put("/api/clients/{id}", clientId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "Updated",
                                  "lastName": "Client",
                                  "phone": "+21699990000",
                                  "address": "Sfax",
                                  "nationalId": "CL-3001",
                                  "dateOfBirth": "1990-01-15"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.address").value("Sfax"));

        mockMvc.perform(get("/api/clients/{id}", clientId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId.toString()));
    }

    @Test
    void adminCanCreateGetUpdateAndSuspendThenActivateContracts() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");

        UUID userId = createUser(adminToken, "contract.owner@insurance.local", "CLIENT");
        UUID clientId = createClient(adminToken, userId, "CL-3002");

        MvcResult createResult = mockMvc.perform(post("/api/contracts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId": "%s",
                                  "contractNumber": "CT-3001",
                                  "type": "HEALTH",
                                  "startDate": "2026-01-01",
                                  "endDate": "2026-12-31",
                                  "coverageLimit": 5000.00,
                                  "reimbursementRate": 0.80,
                                  "status": "ACTIVE"
                                }
                                """.formatted(clientId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andReturn();

        UUID contractId = UUID.fromString(objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asText());

        mockMvc.perform(get("/api/contracts")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.contractNumber=='CT-3001')]").isNotEmpty());

        mockMvc.perform(put("/api/contracts/{id}", contractId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId": "%s",
                                  "contractNumber": "CT-3001",
                                  "type": "HEALTH",
                                  "startDate": "2026-01-01",
                                  "endDate": "2026-12-31",
                                  "coverageLimit": 6500.00,
                                  "reimbursementRate": 0.75
                                }
                                """.formatted(clientId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coverageLimit").value(6500.00))
                .andExpect(jsonPath("$.reimbursementRate").value(0.75));

        mockMvc.perform(patch("/api/contracts/{id}/suspend", contractId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUSPENDED"));

        mockMvc.perform(patch("/api/contracts/{id}/activate", contractId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        mockMvc.perform(get("/api/contracts/{id}", contractId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contractId.toString()));
    }

    @Test
    void clientCanListOwnContractsOnly() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");
        String clientToken = loginAndExtractToken("client@insurance.local", "Password123!");

        mockMvc.perform(post("/api/contracts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId": "44444444-4444-4444-4444-444444444444",
                                  "contractNumber": "CT-CLIENT-SELF",
                                  "type": "HEALTH",
                                  "startDate": "2026-01-01",
                                  "endDate": "2026-12-31",
                                  "coverageLimit": 7500.00,
                                  "reimbursementRate": 0.85,
                                  "status": "ACTIVE"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/contracts/my")
                        .header("Authorization", "Bearer " + clientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.contractNumber=='CT-CLIENT-SELF')]").isNotEmpty());
    }

    @Test
    void validationFailureReturnsBadRequestForContractCreation() throws Exception {
        String adminToken = loginAndExtractToken("admin@insurance.local", "Password123!");

        mockMvc.perform(post("/api/contracts")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "clientId": "%s",
                                  "contractNumber": "",
                                  "type": "HEALTH",
                                  "startDate": "2026-01-01",
                                  "endDate": "2026-12-31",
                                  "coverageLimit": -1,
                                  "reimbursementRate": 1.25,
                                  "status": "ACTIVE"
                                }
                                """.formatted(UUID.randomUUID())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    private UUID createUser(String adminToken, String email, String role) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "Password123!",
                                  "role": "%s",
                                  "enabled": true
                                }
                                """.formatted(email, role)))
                .andExpect(status().isCreated())
                .andReturn();

        return UUID.fromString(objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asText());
    }

    private UUID createClient(String adminToken, UUID userId, String nationalId) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/clients")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "userId": "%s",
                                  "firstName": "Contract",
                                  "lastName": "Owner",
                                  "phone": "+21612344321",
                                  "address": "Ariana",
                                  "nationalId": "%s",
                                  "dateOfBirth": "1989-08-20"
                                }
                                """.formatted(userId, nationalId)))
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
