# Implementation Notes

## PH1-01 Backend scaffold

- The repository started with a minimal backend-first ticket to keep the history aligned with the one-ticket-per-run rule.
- Maven Wrapper was added because the local environment did not provide a `mvn` executable, while Java 21 was available.
- Spring Boot dependencies were limited to web, validation, and test starters so the scaffold does not imply unfinished persistence or security capabilities.
- Package marker files were added to preserve the intended feature-oriented structure in Git before concrete classes exist in those packages.

## PH1-02 PostgreSQL configuration baseline

- PostgreSQL configuration was introduced through a `local` Spring profile rather than the base configuration file to keep shared settings minimal.
- Datasource properties were aligned with future container usage by standardizing on `DB_*` environment variable names early.
- The ticket intentionally stops at connection-property configuration and does not enable schema tooling or database-dependent startup behavior.
- Configuration verification uses property assertions instead of a live connection test so the ticket remains isolated from infrastructure setup.

## Phase 1 backend foundation completion

- Flyway was introduced with a single baseline migration covering all six core tables so the initial schema stays coherent and reviewable.
- Entities were mapped directly to the committed schema using UUID identifiers and explicit relationships rather than inferred conventions.
- DTO work was limited to response boundaries that are already stable, which avoids speculative request payload design before controllers exist.
- MapStruct was added now because the entity graph is still small enough to establish a clear mapper pattern without rework.
- Shared exception handling was introduced before controllers so later API tickets inherit the standardized error shape instead of re-creating it per feature.

## Authentication phase completion

- Spring Security was configured as a stateless JWT-based layer rather than form login or session authentication so the backend remains aligned with the planned Angular frontend.
- A small auth verification surface (`/api/auth/me` plus role-specific endpoints) was added now because role-based authorization is not meaningfully testable without protected routes.
- Flyway seed data was used for default users instead of hardcoded in-memory authentication so login behavior exercises the real persistence path.
- `FlywayConfig` was added explicitly because the current Spring Boot 4 module layout in this project did not auto-run Flyway before JPA validation during tests.
- Jackson Java-time support was added explicitly so the shared `ApiErrorResponse` can serialize `LocalDateTime` consistently across exception handlers and security handlers.

## Phase 3 admin management and OpenAPI

- OpenAPI support uses `springdoc-openapi-starter-webmvc-ui` because the project now has enough endpoint surface to justify in-app API discovery.
- Admin management was kept intentionally narrow: users, clients, and contracts only. Claims remain out of scope so the management APIs stay coherent and verifiable.
- Client creation depends on an existing `CLIENT` user instead of creating both records in one endpoint, which keeps user and client lifecycle concerns explicit.
- Contract status changes are exposed as dedicated action endpoints (`activate`, `suspend`) rather than overloading general update semantics with workflow changes.
- Spring Security rules were made explicit for `/api/users/**`, `/api/clients/**`, `/api/contracts/**`, `/v3/api-docs/**`, and `/swagger-ui/**` so documentation access and admin-only behavior are not left to defaults.

## Phase 4 claims workflow backend

- The first claim workflow pass was kept API-only and persistence-backed. Document handling persists metadata only, which avoids inventing a storage subsystem before Docker and frontend phases exist.
- Client-facing claim routes were separated from reviewer routes inside the same controller because the aggregate is shared but the role and ownership rules differ materially.
- Remaining coverage is derived from already paid claims on the same contract, which keeps reimbursement calculation consistent without introducing a separate ledger table yet.
- Reviewer actions stamp both the reviewer user and review timestamp so future audit-oriented features can build on persisted workflow metadata already present in the schema.
- Claim submission stays constrained to draft claims with at least one document so later UI work can rely on backend-enforced workflow integrity rather than duplicating those rules in Angular.

## Phase 5 complete UI

- Angular Material was chosen deliberately for this phase because the application is a dense internal operations tool, not a marketing site. The priority is predictable tables, forms, badges, drawers, and feedback states.
- The frontend stores the current JWT session in `localStorage` through `AuthStateService`, then applies it uniformly with an HTTP interceptor instead of duplicating token handling across feature services.
- The route model is role-first: the shell is authenticated globally, and feature routes add explicit role checks through `roleGuard` so navigation and authorization rules stay aligned.
- The UI stays close to the backend resource model. Separate API services were created for users, clients, contracts, and claims instead of introducing a broader state library before the application actually needs it.
- A small backend addition, `GET /api/contracts/my`, was necessary to make the client claim-creation UI honest. Without it, the frontend would have needed to misuse admin-only contract endpoints or force manual identifier entry.
