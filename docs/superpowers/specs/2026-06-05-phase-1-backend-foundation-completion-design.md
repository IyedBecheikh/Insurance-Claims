# Phase 1 Backend Foundation Completion Design

## Objective

Complete the remaining backend foundation work from Phase 1 as a single user-approved subphase. This design intentionally overrides the usual one-ticket-per-run rule in `AGENTS.md` to finish the backend foundation in one coherent pass without extending into authentication, business workflows, frontend work, or deployment runtime.

## Scope

This subphase includes:

- adding JPA, Flyway, and MapStruct backend dependencies;
- creating a first Flyway migration for all six core tables:
  - `users`
  - `clients`
  - `contracts`
  - `claims`
  - `claim_documents`
  - `claim_comments`
- adding core enums and JPA entities;
- adding Spring Data repository interfaces;
- adding DTOs for the established domain response boundaries;
- adding shared MapStruct configuration and concrete mappers;
- adding a global exception handler and standard API error response model;
- adding backend tests for mappings and exception handling;
- updating backend architecture, database, testing, implementation notes, API documentation where appropriate, and current repo state documentation.

This subphase excludes:

- authentication and JWT;
- Spring Security configuration;
- controllers and REST endpoints;
- service-layer business workflows;
- Testcontainers;
- Angular work;
- Docker runtime implementation;
- CI pipeline work.

## Approach

Use a balanced backend-foundation approach:

1. establish the relational schema through Flyway;
2. mirror that schema in explicit JPA entities;
3. add repository interfaces for persistence access;
4. define DTO boundaries so later controllers and services do not expose entities;
5. add MapStruct mapping infrastructure now, while the model is still small;
6. introduce shared exception-response infrastructure so later API tickets build on a stable error contract.

This keeps the foundation realistic and enterprise-shaped without prematurely implementing authentication or claims workflows.

## Dependencies

The backend Maven build will add:

- `spring-boot-starter-data-jpa`
- `org.flywaydb:flyway-core`
- `org.flywaydb:flyway-database-postgresql`
- `org.mapstruct:mapstruct`

The Maven compiler configuration will also add MapStruct annotation processing.

Lombok is intentionally excluded for now to avoid adding another tool until the codebase demonstrates a clear need for it.

## Database Design

The first Flyway migration will create all six core tables using UUID primary keys and PostgreSQL-friendly column types.

### Required schema characteristics

- unique `users.email`
- unique `clients.national_id`
- unique `contracts.contract_number`
- unique `claims.claim_number`
- foreign keys for:
  - `clients.user_id`
  - `contracts.client_id`
  - `claims.client_id`
  - `claims.contract_id`
  - `claims.reviewed_by`
  - `claim_documents.claim_id`
  - `claim_comments.claim_id`
  - `claim_comments.author_id`
- non-null constraints for required business fields;
- string-backed enum columns for role and status values;
- timestamp columns for audit-friendly creation/review/upload events where already defined in the product spec.

The migration will not add optional future tables such as `audit_logs`, `notifications`, or `claim_status_events`.

## Java Model

### Enums

Add backend enums for:

- `Role`
- `ClaimStatus`
- `ContractStatus`
- `ContractType`

### Entities

Add JPA entities for:

- `User`
- `Client`
- `Contract`
- `Claim`
- `ClaimDocument`
- `ClaimComment`

Entities will use explicit field mappings and relationships consistent with the first migration. They will remain persistence-internal and will not be exposed through future REST endpoints directly.

## Repositories

Create one Spring Data repository per entity with a minimal set of justified lookup methods, including:

- user lookup by email;
- client lookup by user id;
- contract lookup by contract number and by client id;
- claim lookup by claim number and by client id;
- document lookup by claim id;
- comment lookup by claim id.

No speculative query layer will be added.

## DTOs and Mapping

### DTO scope

The initial DTO layer will focus on stable response boundaries:

- `UserResponseDto`
- `ClientResponseDto`
- `ContractResponseDto`
- `ClaimResponseDto`
- `ClaimDocumentResponseDto`
- `ClaimCommentResponseDto`

This subphase will avoid speculative create/update request DTOs where controller behavior is not defined yet.

### Mapping

Add:

- a shared `MapperConfig`;
- concrete MapStruct mappers for user, client, contract, claim, document, and comment;
- mapper composition where nested DTO relationships need to be expressed cleanly.

The goal is to prove the entity-to-DTO boundary and establish the mapper pattern for later feature tickets.

## Exception Handling

Add:

- `ApiErrorResponse` in the shared response package;
- custom exceptions where necessary for not-found or invalid-state patterns;
- `GlobalExceptionHandler` using `@RestControllerAdvice`.

The handler will cover:

- bean validation errors;
- `IllegalArgumentException`;
- `IllegalStateException`;
- not-found exceptions;
- generic fallback exceptions.

The response structure will align with the standard error shape already defined in `AGENTS.md`.

## Testing

The subphase will keep the existing configuration and context tests and add focused backend tests for:

- mapper behavior for at least one core aggregate and nested relationships;
- global exception handler behavior for validation-style and generic failures;
- schema presence indirectly through build/startup integration, without requiring a live PostgreSQL instance in unit tests.

This phase will not introduce Testcontainers yet. That belongs to a later test-focused phase unless a backend endpoint requires it sooner.

## Documentation Changes

The subphase will create or update:

- `docs/05-database-design.md`
- `docs/06-api-documentation.md`
- `docs/09-backend-architecture.md`
- `docs/10-testing-strategy.md`
- `docs/14-implementation-notes.md`
- `docs/Repo_Current_State.md`

Documentation will clearly distinguish between completed backend foundation work and still-missing API/auth workflow features.

## Risks and Constraints

- This is a broader-than-normal change set, so verification discipline matters more than usual.
- DTO boundaries may need refinement once controllers and services are introduced, but they should still be stable enough for read models now.
- The migration and entities must stay aligned; otherwise later persistence work will become fragile.
- No endpoint documentation should imply runnable REST resources that do not exist yet.

## Completion Criteria

This subphase is complete when:

1. all six core tables exist in the first Flyway migration;
2. corresponding JPA entities and repositories compile cleanly;
3. DTOs and MapStruct mappers exist for the agreed response boundaries;
4. global exception handling infrastructure is present;
5. backend tests pass fresh;
6. documentation reflects the actual backend foundation state without overstating runtime capabilities.
