# Testing Strategy

## Current baseline

The repository now includes focused backend and frontend tests for the implemented scope.

## Backend tests

- `InsuranceClaimsApplicationTests` verifies that the full application context starts with the H2-backed `test` profile.
- `LocalDatasourceConfigurationTests` verifies that the `local` profile resolves the expected PostgreSQL datasource properties without requiring a live database.
- `ClaimMapperTests` verifies that claim entity data maps into the agreed DTO boundary, including nested documents and comments.
- `GlobalExceptionHandlerTests` verifies the shared error contract for validation failures, not-found errors, and unexpected exceptions.
- `AuthIntegrationTests` verifies:
  - successful login for seeded `ADMIN`, `AGENT`, and `CLIENT` users;
  - failed login for invalid credentials;
  - `401` behavior for missing/invalid tokens;
  - `403` behavior for role mismatch;
  - role-appropriate access to the auth verification endpoints.
- `AdminManagementIntegrationTests` verifies:
  - OpenAPI JSON exposure;
  - admin-only access to users, clients, and contracts APIs;
  - successful create/list/get/update flows;
  - enable/disable user behavior;
  - contract suspend/activate transitions;
  - validation failure on invalid contract input;
  - client-scoped contract listing through `/api/contracts/my`.
- `ClaimWorkflowIntegrationTests` verifies:
  - client draft claim creation;
  - client document metadata persistence and submit rules;
  - client self-service listing and detail retrieval;
  - reviewer transition flow from `SUBMITTED` to `UNDER_REVIEW`, `APPROVED`, and `PAID`;
  - reimbursement capping against remaining contract coverage;
  - reviewer filtering by claim status.
- The current backend suite is executed with Maven Wrapper using `.\mvnw.cmd test`.

## Frontend tests

- `auth-state.service.spec.ts` verifies session persistence and clearing behavior.
- `role.guard.spec.ts` verifies redirect behavior for unauthenticated and role-matched access.
- `login-page.component.spec.ts` verifies login submission and role-based redirect behavior.
- The current frontend suite is executed with:

```powershell
cd frontend
npx ng test --watch false --browsers ChromeHeadless --progress=false
```

## Scope notes

The auth integration tests use an H2 in-memory database running the committed Flyway migrations plus seeded auth data. This keeps the tests fast while still exercising persisted authentication state.

Current gaps:

- no Testcontainers-backed PostgreSQL integration tests yet;
- no frontend E2E suite yet.
