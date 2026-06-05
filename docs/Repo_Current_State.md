# Repo Current State

## Current branch

`master`

## Completed tickets

- `PH1-01` Backend scaffold with Spring Boot 4, Java 21, Maven Wrapper, package skeleton, and baseline documentation.
- `PH1-02` PostgreSQL configuration baseline with local profile defaults and Docker-aligned environment variable names.
- `PH1-COMPLETE` Remaining backend foundation: Flyway baseline, core entities, repositories, DTOs, MapStruct, and global exception handling.
- `PH2-COMPLETE` Authentication phase: Spring Security, JWT login, role-based auth endpoints, and seeded default users.
- `PH3-COMPLETE` OpenAPI baseline plus admin management endpoints for users, clients, and contracts.
- `PH4-COMPLETE` Claims workflow backend: client claim creation/submission, persisted document metadata, reviewer actions, and reimbursement calculation.
- `PH5-COMPLETE` Angular UI: login, role-aware dashboards, admin management screens, claim workflow screens, screenshots, and GitHub-ready README.

## Current folder structure

```text
backend/
frontend/
docs/
.github/workflows/
```

## Backend status

- Spring Boot application bootstrap class exists.
- Maven project uses Java 21 and Spring Boot 4.
- Maven Wrapper is present for reproducible local builds.
- PostgreSQL JDBC dependency is configured.
- Spring Data JPA, Flyway, and MapStruct are configured.
- Local datasource configuration is defined in `application-local.yml`.
- Core entities exist for users, clients, contracts, claims, claim documents, and claim comments.
- Repository interfaces exist for all core entities.
- Response DTOs and mappers exist for the core aggregates.
- Shared API error response and global exception handler are implemented.
- Spring Security is configured for stateless JWT authentication.
- Auth endpoints exist for login, authenticated identity lookup, and per-role authorization checks.
- Flyway seed data creates one default enabled user for each supported role plus a linked client record.
- OpenAPI JSON and Swagger UI are configured.
- Admin-only endpoints exist for user creation/listing/enabling, client creation/listing/updating, and contract creation/listing/updating/status changes.
- Claim endpoints now exist for client self-service creation/list/detail/document submission and reviewer list/detail/status actions.
- Claim workflow enforces contract ownership, active-contract eligibility, service-date checks, required documents for submission, reviewer transitions, and reimbursement calculation.
- Client-scoped contract listing exists through `/api/contracts/my` to support the Angular claim workflow.
- Backend tests cover scaffold startup, local datasource property resolution, claim mapping, exception handling, auth integration behavior, admin management integration behavior, and claim workflow integration behavior.

## Frontend status

- Angular 20 application scaffolded with Angular Material.
- Core auth state, JWT interceptor, route guards, and role-aware shell are implemented.
- Screens exist for login, dashboards, users, clients, contracts, client claims, and reviewer claims.
- Screenshots for the major UI surfaces are stored in `docs/assets/`.

## Installed dependencies

### Backend

- `org.springframework.boot:spring-boot-starter-web`
- `org.springframework.boot:spring-boot-starter-data-jpa`
- `org.springframework.boot:spring-boot-starter-validation`
- `org.springframework.boot:spring-boot-starter-security`
- `org.flywaydb:flyway-core`
- `org.flywaydb:flyway-database-postgresql`
- `org.mapstruct:mapstruct`
- `org.springdoc:springdoc-openapi-starter-webmvc-ui`
- `io.jsonwebtoken:jjwt-api`
- `io.jsonwebtoken:jjwt-impl`
- `io.jsonwebtoken:jjwt-jackson`
- `com.fasterxml.jackson.datatype:jackson-datatype-jsr310`
- `org.postgresql:postgresql`
- `org.springframework.boot:spring-boot-starter-test`
- `org.springframework.security:spring-security-test`
- `com.h2database:h2`

### Frontend

- `@angular/animations`
- `@angular/cdk`
- `@angular/common`
- `@angular/compiler`
- `@angular/core`
- `@angular/forms`
- `@angular/material`
- `@angular/platform-browser`
- `@angular/router`
- `playwright` (dev dependency for local screenshot automation)

## Available scripts

### Backend

- `.\mvnw.cmd clean test`
- `.\mvnw.cmd test`
- `.\mvnw.cmd spring-boot:run`
- `.\mvnw.cmd flyway:migrate`

### Frontend

- `npm start`
- `npm run build`
- `npx ng test --watch false --browsers ChromeHeadless --progress=false`

## Build/test status

- Backend: `.\mvnw.cmd test` passed on 2026-06-05 with 27 tests, 0 failures, 0 errors, 0 skipped.
- Frontend: `npx ng test --watch false --browsers ChromeHeadless --progress=false` passed on 2026-06-05 with 5 tests, 0 failures.
- Frontend: `npm run build` passed on 2026-06-05.
- Docker: skipped because `docker-compose.yml` is still a placeholder only.

## Known issues

- Document handling stores metadata only; binary upload/storage is not implemented yet.
- Standalone browser screenshots were captured against mocked API responses for stability; the live frontend/backend browser path was not fully exercised end to end.
- Docker Compose is a placeholder only.

## Next recommended ticket

- `PH6-01` Add Dockerfiles and a real `docker-compose.yml` for backend, frontend, and PostgreSQL local runtime.
