# Backend Architecture

## Foundation

The backend is initialized as a Maven-based Spring Boot application targeting Java 21 and Spring Boot 4.

The project now includes PostgreSQL JDBC, Spring Data JPA, Flyway, MapStruct, Spring Security, JJWT, and Jackson Java-time support as the current backend baseline.

## Package structure

The package root is `com.iyed.insuranceclaims`. The scaffold creates the feature and shared packages expected by the project rules:

- `auth`
- `user`
- `client`
- `contract`
- `claim`
- `document`
- `dashboard`
- `common`
- `config`

The package skeleton is now partially populated:

- `auth` contains login DTOs, controller, service, JWT support, user-details integration, and security handlers;
- `user`, `client`, `contract`, `claim`, and `document` contain the current persistence-side entities, repositories, DTOs, and mappers;
- `common` contains error handling and shared response types;
- `config` contains explicit Flyway and Jackson configuration needed by the current Boot 4 setup.

## Configuration baseline

- `application.yml` contains shared application settings.
- `application-local.yml` contains local PostgreSQL datasource defaults.
- `application-test.yml` provides the H2-backed integration-test profile.
- Datasource values resolve from `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD`.
- Flyway migrations live under `db/migration`.
- Local JPA configuration uses `ddl-auto=validate` to avoid relying on Hibernate schema generation.
- Flyway is wired explicitly through `FlywayConfig` so schema migration still runs before JPA validation in the current Spring Boot 4 module layout.

The backend now has committed JPA mappings and Flyway migration tooling. Automatic Hibernate schema generation is still not used as the committed schema-management mechanism.

## Persistence model

The backend foundation now models the six core tables through JPA entities:

- `User`
- `Client`
- `Contract`
- `Claim`
- `ClaimDocument`
- `ClaimComment`

Each entity is paired with a repository interface and remains internal to persistence. DTO records and MapStruct mappers provide the response boundary for later controllers and services.

## Authentication module

The `auth` module currently includes:

- `AuthController` for HTTP auth endpoints;
- `AuthService` for login orchestration and authenticated-user projection;
- `CustomUserDetailsService` and `CustomUserPrincipal` for Spring Security integration;
- `JwtService` for token generation and validation;
- `JwtAuthenticationFilter` for bearer-token processing;
- `SecurityConfig` for stateless security rules;
- `RestAuthenticationEntryPoint` and `RestAccessDeniedHandler` for predictable security error responses.

## Admin management modules

The backend now includes concrete management controllers and services for:

- `user`
- `client`
- `contract`

The current pattern is:

- controller for HTTP contract and validation entry;
- service for orchestration and business checks;
- repository for persistence;
- mapper for entity-to-response projection.

Request DTOs were added only where input contracts are now stable:

- `CreateUserRequestDto`
- `CreateClientRequestDto`
- `UpdateClientRequestDto`
- `CreateContractRequestDto`
- `UpdateContractRequestDto`

The contract module now also exposes a client-scoped read path through `GET /api/contracts/my` so the Angular client workflow can create claims against real owned contracts without reusing admin endpoints.

## Claim workflow module

The `claim` module now includes:

- `ClaimController` for client and reviewer HTTP endpoints;
- `ClaimService` for workflow rules, ownership checks, and reimbursement calculation;
- `ClaimRepository` with repository and specification support for reviewer filtering;
- `CreateClaimRequestDto` as the client draft-creation contract.

The `document` module now also includes `AddClaimDocumentRequestDto` for persisted claim document metadata.

Current service responsibilities:

- resolve the authenticated client from the authenticated user;
- enforce contract ownership, active status, and service-date validity;
- keep document attachment and submission limited to draft claims;
- enforce claim status transitions;
- assign reviewer identity and review timestamp;
- calculate reimbursement from contract reimbursement rate and remaining paid coverage.

## Shared infrastructure

- `CentralMapperConfig` standardizes MapStruct behavior.
- `ApiErrorResponse` and `GlobalExceptionHandler` establish a consistent API error contract before endpoint work begins.
