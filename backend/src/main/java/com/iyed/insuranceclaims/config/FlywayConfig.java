package com.iyed.insuranceclaims.config;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.boot.jpa.autoconfigure.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
    }

    @Configuration
    static class FlywayEntityManagerDependencyConfig extends EntityManagerFactoryDependsOnPostProcessor {

        FlywayEntityManagerDependencyConfig() {
            super("flyway");
        }
    }
}
