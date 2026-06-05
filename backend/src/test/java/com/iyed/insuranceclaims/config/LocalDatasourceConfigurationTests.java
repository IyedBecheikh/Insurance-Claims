package com.iyed.insuranceclaims.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = LocalDatasourceConfigurationTests.TestApplication.class)
@ActiveProfiles("local")
class LocalDatasourceConfigurationTests {

    @Autowired
    private Environment environment;

    @Test
    void localProfileExposesDefaultPostgresqlDatasourceProperties() {
        assertThat(environment.getProperty("spring.datasource.url"))
                .isEqualTo("jdbc:postgresql://localhost:5432/claims_db");
        assertThat(environment.getProperty("spring.datasource.username"))
                .isEqualTo("claims_user");
        assertThat(environment.getProperty("spring.datasource.password"))
                .isEqualTo("claims_password");
        assertThat(environment.getProperty("spring.datasource.driver-class-name"))
                .isEqualTo("org.postgresql.Driver");
    }

    @SpringBootConfiguration
    static class TestApplication {
    }
}
