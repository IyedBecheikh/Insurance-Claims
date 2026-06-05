# PH1-01 Backend Scaffold Design

## Ticket

`PH1-01 Backend scaffold`

## Objective

Create the initial backend project scaffold for the Insurance Claims Management System as a single, isolated Phase 1 ticket. The output must establish the repository and backend structure needed for later tickets without pre-implementing persistence, security, or business features.

## Scope

This ticket includes:

- initializing the repository as a Git repository;
- creating the required top-level repository structure for `backend`, `frontend`, `docs`, and `.github/workflows`;
- scaffolding a Spring Boot backend project in `backend/`;
- targeting Java 21 and Spring Boot 4 in Maven configuration;
- creating the base package `com.iyed.insuranceclaims`;
- adding the minimal backend application entry point and package skeleton;
- adding baseline documentation that is already justified by the scaffold;
- verifying that the backend scaffold builds successfully.

This ticket excludes:

- PostgreSQL datasource configuration;
- Flyway migrations;
- JPA entities and repositories;
- DTO implementations;
- MapStruct configuration;
- Spring Security and JWT;
- Angular application setup;
- Docker runtime wiring;
- CI workflow implementation.

## Approach

The backend will be created as a plain Maven-based Spring Boot application under `backend/`. The Maven build will include only dependencies needed to compile and test a minimal web application scaffold:

- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `spring-boot-starter-test`

Persistence and security dependencies are deferred to later tickets to preserve a clean ticket boundary and avoid creating partially configured infrastructure that the project cannot use yet.

## Repository Structure

The ticket will create this initial shape:

```text
Insurance Claims/
├── .github/
│   └── workflows/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/iyed/insuranceclaims/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
├── docs/
│   ├── assets/
│   ├── 01-project-overview.md
│   ├── 04-technical-architecture.md
│   ├── 09-backend-architecture.md
│   ├── 14-implementation-notes.md
│   ├── 15-roadmap.md
│   ├── Repo_Current_State.md
│   └── Skill_log.md
├── frontend/
├── AGENTS.md
├── LICENSE
├── README.md
└── docker-compose.yml
```

Some files outside `backend/` will be placeholders or initial documentation only. They exist to align the repository with the required public structure, not to implement future features.

## Backend Structure

The Java package skeleton will follow the structure required by `AGENTS.md`:

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

Only package markers and the main application class will be created in this ticket. Feature code remains out of scope.

## Configuration

`pom.xml` will define:

- Java 21 toolchain target;
- Spring Boot parent;
- standard jar packaging;
- minimal compiler and Spring Boot plugin configuration.

`application.yml` will define only the application name and a conservative baseline configuration that does not assume database, security, or external services are available.

## Documentation Changes

The ticket will create or populate:

- `README.md` with an initial project overview and repository layout summary;
- `backend/README.md` with backend bootstrap instructions;
- `docs/01-project-overview.md`;
- `docs/04-technical-architecture.md`;
- `docs/09-backend-architecture.md`;
- `docs/14-implementation-notes.md`;
- `docs/15-roadmap.md`;
- `docs/Repo_Current_State.md`.

Documentation will state clearly that only the backend scaffold exists at this stage.

## Verification

The primary verification for this ticket is:

```bash
cd backend
mvn clean test
```

If the environment does not have the required JDK or Maven available, the ticket cannot be marked complete. In that case the failure must be reported explicitly.

Frontend, Docker, and browser verification are out of scope for this scaffold ticket and will be reported as skipped with reason.

## Risks and Constraints

- Spring Boot 4 availability depends on the local and remote Maven environment resolving the selected version.
- Creating placeholder top-level files must not drift into implementing later tickets.
- Documentation must not claim database, auth, Docker, or frontend capabilities that do not exist yet.

## Completion Criteria

The ticket is complete when:

1. the repository is initialized with Git;
2. the required top-level folders and backend project structure exist;
3. the backend Maven project compiles and tests successfully;
4. scaffold-relevant documentation is present and accurate;
5. `docs/Repo_Current_State.md` reflects the actual scaffold state.
