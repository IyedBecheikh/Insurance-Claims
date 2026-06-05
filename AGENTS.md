# AGENTS.md

## Purpose

This file defines the operating rules for AI agents working on the **Insurance Claims Management System** repository.

The project is a full-stack application built to demonstrate practical experience with **Java 21, Spring Boot 4, Angular, PostgreSQL, Docker, CI/CD, testing, and end-to-end technical documentation**.

Follow these rules for every ticket unless the user explicitly overrides them.

---

## Project Context

The application manages a simplified health-insurance claim workflow:

```text
CLIENT creates claim → uploads documents → submits claim
AGENT reviews claim → approves/rejects claim → adds comments
ADMIN manages users, clients, contracts, and system configuration
```

The project must be written and documented as if it were an enterprise application, not a throwaway CRUD demo.

The repository should provide a complete, public GitHub record of the system, including:

- clear product scope and business rules;
- documented backend, frontend, database, security, testing, and deployment decisions;
- reproducible local setup using Docker Compose;
- verifiable API behavior through Swagger/OpenAPI and Markdown documentation;
- realistic implementation notes that make the project understandable without private context.

---

## Core Workflow Rules

| Rule | Requirement |
| --- | --- |
| One ticket only | Implement exactly one ticket per run. |
| No future work | Do not implement future-ticket features early. |
| Scoped changes | Do not refactor unrelated modules. |
| Architecture restraint | Do not introduce new architecture unless the active ticket requires it. |
| Dependency restraint | Avoid unnecessary Maven/npm dependencies. |
| Documentation first | Update or create relevant docs for every meaningful feature. |
| Verification | Run backend and frontend build/tests when possible. |
| Reporting | Report files changed, commands run, build/test results, manual checks, risks, and follow-ups. |
| Durable mutations | Any business state change must persist through the backend API and PostgreSQL. Do not keep real business state only in Angular component state. |

---

## Repository Sources of Truth

| File | Purpose |
| --- | --- |
| `README.md` | Main project overview, stack, setup, screenshots, and links to docs. |
| `docs/01-project-overview.md` | Product context, users, goals, and scope. |
| `docs/02-business-requirements.md` | Business rules for clients, contracts, claims, and reimbursements. |
| `docs/03-functional-specification.md` | User-facing features and workflows. |
| `docs/04-technical-architecture.md` | Backend/frontend architecture and system design. |
| `docs/05-database-design.md` | Entity model, relationships, migrations, and schema notes. |
| `docs/06-api-documentation.md` | REST API endpoints, request/response examples, roles, and errors. |
| `docs/07-security-design.md` | JWT authentication, RBAC, route protection, and security assumptions. |
| `docs/08-frontend-architecture.md` | Angular modules, services, guards, interceptors, forms, and UI structure. |
| `docs/09-backend-architecture.md` | Spring Boot packages, layers, DTOs, services, repositories, and exceptions. |
| `docs/10-testing-strategy.md` | Unit, integration, frontend, and E2E test strategy. |
| `docs/11-ci-cd.md` | GitHub Actions/GitLab CI pipeline documentation. |
| `docs/12-docker-deployment.md` | Docker and Docker Compose setup. |
| `docs/13-troubleshooting.md` | Known setup issues and fixes. |
| `docs/14-implementation-notes.md` | Detailed implementation notes, design tradeoffs, and technical explanations. |
| `docs/15-roadmap.md` | Planned improvements and optional advanced features. |
| `docs/Repo_Current_State.md` | Living snapshot of current implementation state. |
| `AGENTS.md` | Agent workflow and implementation rules. |

If a file listed above does not exist yet, create it when the active ticket needs it.

---

## Tech Stack Reference

### Backend

| Layer | Choice |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4 |
| API | Spring Web REST controllers |
| Security | Spring Security + JWT + BCrypt |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL |
| Migrations | Flyway |
| Build tool | Maven |
| Mapping | MapStruct, unless simple manual mapping is clearer |
| Boilerplate | Lombok, if already configured |
| API docs | Swagger / OpenAPI |
| Tests | JUnit 5, Mockito, Spring Boot Test, Testcontainers |
| Code quality | Checkstyle and/or SonarQube/SonarCloud |

### Frontend

| Layer | Choice |
| --- | --- |
| Language | TypeScript |
| Framework | Angular 15+ |
| UI | Angular Material or simple custom components |
| Forms | Angular Reactive Forms |
| Routing | Angular Router |
| Auth | Route guards + HTTP interceptor |
| Async handling | RxJS |
| Charts | Angular-compatible chart library only if dashboard ticket requires it |
| Tests | Jasmine/Karma by default, Cypress or Playwright for E2E if configured |
| Linting | ESLint |

### DevOps

| Layer | Choice |
| --- | --- |
| Version control | Git + GitHub |
| Local orchestration | Docker Compose |
| Containers | Dockerfiles for backend and frontend |
| CI/CD | GitHub Actions or GitLab CI |
| Runtime database | PostgreSQL container for local development |
| Optional deployment target | Render, Railway, Fly.io, VPS, or OpenShift-compatible deployment notes |

Do not introduce additional libraries or frameworks without a strong reason. If a new dependency is added, document why in `docs/Repo_Current_State.md` and the end-of-run report.

---

## Expected Repository Structure

```text
insurance-claims-management-system/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/iyed/insuranceclaims/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── frontend/
│   ├── src/
│   ├── angular.json
│   ├── package.json
│   ├── Dockerfile
│   └── README.md
├── docs/
│   ├── assets/
│   ├── 01-project-overview.md
│   ├── 02-business-requirements.md
│   ├── 03-functional-specification.md
│   ├── 04-technical-architecture.md
│   ├── 05-database-design.md
│   ├── 06-api-documentation.md
│   ├── 07-security-design.md
│   ├── 08-frontend-architecture.md
│   ├── 09-backend-architecture.md
│   ├── 10-testing-strategy.md
│   ├── 11-ci-cd.md
│   ├── 12-docker-deployment.md
│   ├── 13-troubleshooting.md
│   ├── 14-implementation-notes.md
│   ├── 15-roadmap.md
│   └── Repo_Current_State.md
├── .github/
│   └── workflows/
│       └── ci.yml
├── docker-compose.yml
├── README.md
├── AGENTS.md
└── LICENSE
```

Do not collapse backend, frontend, and docs into one folder. The separation is intentional and should remain clear for portfolio review.

---

## Backend Module Structure

Use a feature-oriented package structure with clear layers inside each feature.

```text
com.iyed.insuranceclaims
├── auth
│   ├── controller
│   ├── dto
│   ├── security
│   └── service
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
├── client
├── contract
├── claim
├── document
├── dashboard
├── common
│   ├── exception
│   ├── response
│   └── validation
└── config
```

Rules:

- Controllers handle HTTP only.
- Services contain business logic.
- Repositories handle database access.
- Entities must not be exposed directly through REST responses.
- Use DTOs for request and response payloads.
- Use global exception handling for predictable errors.
- Keep reimbursement calculation logic testable.
- Keep security rules explicit and documented.

---

## Frontend Module Structure

Use a modular Angular structure.

```text
frontend/src/app
├── core
│   ├── auth
│   ├── guards
│   ├── interceptors
│   └── services
├── shared
│   ├── components
│   ├── models
│   └── pipes
├── features
│   ├── auth
│   ├── dashboard
│   ├── users
│   ├── clients
│   ├── contracts
│   └── claims
└── layout
```

Rules:

- Keep API calls in Angular services.
- Use route guards for protected routes.
- Use an HTTP interceptor for JWT.
- Use reactive forms for create/edit screens.
- Do not duplicate TypeScript interfaces across features if a shared model fits.
- Component state is allowed for UI-only state, not persisted business state.

---

## UI Rules

The UI should look like a professional internal insurance back-office system.

Preferred style:

- clean admin dashboard;
- light theme by default;
- readable tables;
- clear status badges;
- form validation messages;
- role-specific navigation;
- simple dashboard cards;
- consistent spacing and typography.

Required UI areas:

| Area | Notes |
| --- | --- |
| Login page | Simple, centered login form. |
| Admin dashboard | Users, clients, contracts, global claim metrics. |
| Agent dashboard | Claims waiting for review and recent decisions. |
| Client dashboard | My contracts, my claims, reimbursement status. |
| Claims list | Filters by status, date, client, claim number. |
| Claim detail | Claim data, documents, comments, workflow actions. |
| Contract screens | Create, list, view, activate/suspend contracts. |

Screenshots and mockups belong in `docs/assets/`.

---

## Domain Rules

## Users and Roles

Valid roles:

```text
ADMIN
AGENT
CLIENT
```

Do not invent new roles unless a ticket explicitly requires it.

## Claim Status System

Valid claim statuses:

```text
DRAFT → SUBMITTED → UNDER_REVIEW → APPROVED → PAID
                         ↓
                      REJECTED
```

Allowed status values:

```text
DRAFT
SUBMITTED
UNDER_REVIEW
APPROVED
REJECTED
PAID
```

Rules:

- A client can create a claim in `DRAFT`.
- A claim must have at least one document before submission.
- A client can submit only their own claim.
- An agent or admin can move a submitted claim to `UNDER_REVIEW`.
- An agent or admin can approve or reject a claim under review.
- A rejected claim does not consume contract coverage.
- A paid claim consumes contract coverage.
- Do not invent custom status values.

## Contract Status System

Valid contract statuses:

```text
ACTIVE
EXPIRED
SUSPENDED
```

Rules:

- Claims can be created only under an active contract.
- The contract date range must contain the medical service date.
- Coverage limit must be positive.
- Reimbursement rate must be between `0` and `1`.

## Reimbursement Rule

Use this core formula:

```text
reimbursementAmount = min(claimAmount × reimbursementRate, remainingCoverage)
```

Rules:

- Claim amount must be greater than zero.
- Reimbursement cannot exceed remaining coverage.
- Paid claims reduce remaining coverage.
- Rejected claims do not reduce remaining coverage.

---

## Database Rules

- Use PostgreSQL as the database.
- Use Flyway migrations for schema changes.
- Do not rely on Hibernate auto-DDL for committed schema evolution.
- Use UUID primary keys unless a ticket requires otherwise.
- Use database constraints for uniqueness and required relationships.
- Keep seed data realistic but minimal.
- Never hardcode business records in backend services or Angular components.

Expected core tables:

```text
users
clients
contracts
claims
claim_documents
claim_comments
```

Optional later tables:

```text
audit_logs
notifications
claim_status_events
```

---

## API Rules

- Use RESTful endpoints under `/api`.
- Use plural resource names: `/api/claims`, `/api/contracts`, `/api/users`.
- Use DTOs for every request and response.
- Validate request bodies using Bean Validation.
- Document endpoints with Swagger/OpenAPI.
- Also document important endpoints in `docs/06-api-documentation.md`.
- Return consistent error responses.

Standard error response shape:

```json
{
  "timestamp": "2026-06-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Claim amount must be greater than zero",
  "path": "/api/claims"
}
```

---

## Security Rules

- Passwords must be hashed with BCrypt.
- Use JWT for authentication.
- Protect backend endpoints with Spring Security.
- Protect frontend routes with Angular guards.
- Use role-based authorization for admin, agent, and client actions.
- Clients must only access their own claims and contracts.
- Agents can review claims but cannot manage system users unless explicitly allowed.
- Admins can manage users, clients, contracts, and claim configuration.
- Do not store plaintext secrets in the repository.
- Use environment variables for secrets and database credentials.

---

## Documentation Rules

Documentation is a first-class deliverable for this project.

Every meaningful feature must include relevant documentation updates.

Required documentation updates by change type:

| Change type | Required docs |
| --- | --- |
| New backend feature | `docs/09-backend-architecture.md`, `docs/06-api-documentation.md` |
| New frontend feature | `docs/08-frontend-architecture.md` |
| New entity/table | `docs/05-database-design.md` |
| New security behavior | `docs/07-security-design.md` |
| New workflow/business rule | `docs/02-business-requirements.md`, `docs/03-functional-specification.md` |
| New tests | `docs/10-testing-strategy.md` |
| New Docker behavior | `docs/12-docker-deployment.md` |
| New CI/CD behavior | `docs/11-ci-cd.md` |
| New implementation detail or design decision | `docs/14-implementation-notes.md` |

Documentation should explain:

- what was implemented;
- why this design was chosen;
- how it works;
- how to configure and run it;
- how to test it;
- operational assumptions, limitations, and tradeoffs.

Avoid vague documentation such as “added service” without explaining the responsibility of that service.

---

## Living Repo State

After each completed ticket, update `docs/Repo_Current_State.md`.

The document must include:

| Section | Expected content |
| --- | --- |
| Current branch | Active Git branch. |
| Completed tickets | Ticket IDs completed so far, with short notes. |
| Current folder structure | Concise tree of important repository folders and files. |
| Backend status | Implemented modules, endpoints, entities, tests. |
| Frontend status | Implemented routes, components, services, guards. |
| Installed dependencies | Backend Maven dependencies and frontend npm dependencies. |
| Available scripts | Backend, frontend, Docker, test, lint, and migration commands. |
| Build/test status | Latest command results and skipped checks with reasons. |
| Known issues | Current blockers, limitations, or technical debt. |
| Next recommended ticket | Next logical ticket from `docs/15-roadmap.md` or `docs/Tickets.md`, if present. |

---

## Verification Commands

Run the most relevant commands after each ticket.

### Backend

```bash
cd backend
mvn clean verify
```

If only compiling is realistic:

```bash
cd backend
mvn clean package -DskipTests
```

### Frontend

```bash
cd frontend
npm install
npm run build
npm test -- --watch=false
```

Use `npm`, `pnpm`, or `yarn` consistently based on the existing lockfile.

### Docker

```bash
docker compose up --build
```

### Manual verification

| Target | URL |
| --- | --- |
| Backend API | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| Angular app | `http://localhost:4200` |
| PostgreSQL | `localhost:5432` |

For non-UI tickets, browser verification may be skipped, but the reason must be reported.

If the build fails, report the error and do not mark the ticket complete.

---

## End-of-Run Report

At the end of every run, report:

| Item | Required details |
| --- | --- |
| Summary of changes | Short description of what changed. |
| Files changed | List of touched files. |
| Backend verification | Maven build/test results or reason skipped. |
| Frontend verification | Angular build/test results or reason skipped. |
| Docker verification | Docker Compose result or reason skipped. |
| Manual verification | What was checked in browser, Swagger, or API client. |
| Documentation updated | Docs that were changed. |
| Risks | Remaining risks or assumptions. |
| Follow-up tickets | Recommended next tickets or cleanup tasks. |

---

## Implementation Notes Rules

When implementing a meaningful feature, add concise technical notes to `docs/14-implementation-notes.md`. These notes must be written as public project documentation, not internal learning notes.

Examples:

| Feature | Implementation notes to document |
| --- | --- |
| Authentication | JWT flow, BCrypt password hashing, Spring Security configuration, stateless request handling. |
| Claim API | REST resource design, DTO boundaries, validation rules, service responsibilities, repository usage. |
| Database migrations | Flyway migration naming, schema versioning, constraints, foreign keys, rollback considerations if relevant. |
| Angular login | Reactive form structure, auth service flow, interceptor behavior, route guard behavior. |
| Claim workflow | State transitions, allowed actions by role, business validation, reimbursement calculation. |
| Tests | Tested behavior, unit/integration boundaries, use of mocks or Testcontainers. |
| Docker | Image structure, container networking, environment variables, startup order, local development assumptions. |

Keep the notes practical, specific to this codebase, and suitable for public GitHub readers.

---

## Optional Advanced Features

Do not implement these unless the active ticket explicitly requests them:

- Kafka claim status events;
- Elasticsearch advanced search;
- Redis caching;
- Camunda workflow engine;
- microservices split;
- OpenShift deployment;
- email notifications;
- PDF export;
- audit logs.

If added, document the reason and the architecture impact.

---

## Skill Usage Logging

When using a reusable skill, append one line to `docs/Skill_log.md`.

Log only after the skill is done.

Format:

```json
{"ts":"YYYY-MM-DD","skill":"skill-name","task":"short task label","outcome":"helped|neutral|hurt","notes":"<=120 chars"}
```

Rules:

- Do not include full prompts, code dumps, stack traces, or private data.
- `helped` = saved time, avoided mistakes, or produced reusable output.
- `neutral` = worked but no clear advantage.
- `hurt` = caused wrong output, confusion, rework, bugs, or token waste.
- Keep notes short.
- If multiple skills are used, log one line per skill.


---

## Reusable skills
When a major component is completed, or when a task takes repeated attempts due to configuration issues, framework behavior, tooling limits, or unclear project constraints, append one concise line to `docs/Skill_log.md` noting that a reusable skill would be beneficial and why. Keep the note short and implementation-focused.
