# PH1-02 PostgreSQL Configuration Design

## Ticket

`PH1-02 PostgreSQL configuration baseline`

## Objective

Introduce the PostgreSQL configuration baseline for local development without implementing schema management, Docker runtime behavior, or persistence-layer code. The goal is to make the backend configuration ready for later database work while preserving a strict single-ticket scope.

## Scope

This ticket includes:

- adding the PostgreSQL JDBC dependency to the backend Maven build;
- introducing datasource configuration properties for local development;
- separating common configuration from local database configuration through a Spring profile;
- selecting environment variable names that can be reused later by Docker Compose;
- adding automated verification for the resolved datasource properties;
- updating documentation for backend configuration, testing, Docker assumptions, implementation notes, and current repo state.

This ticket excludes:

- Flyway;
- JPA entities or repositories;
- schema creation or migrations;
- live database startup automation;
- Docker Compose implementation;
- security changes;
- API endpoints.

## Recommended Approach

Use a two-file configuration layout:

- `application.yml` for common application settings;
- `application-local.yml` for local PostgreSQL datasource defaults.

The `local` profile will resolve configuration from environment variables with sensible local fallbacks. This keeps the current configuration small while avoiding a future rename when Docker support is added.

## Configuration Model

The datasource configuration will use these environment variables:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USERNAME`
- `DB_PASSWORD`

The local profile will default to:

- host: `localhost`
- port: `5432`
- database: a development-safe database name
- username/password: development-safe defaults that can be overridden by environment variables

The ticket will configure only the datasource URL, username, password, and driver class name. It will not enable schema generation or migration tooling.

## Testing Strategy

The ticket will keep the existing context smoke test and add a focused configuration test that verifies datasource properties under the `local` profile without requiring a running PostgreSQL instance.

The test should validate resolved property values rather than connection success. This keeps the ticket isolated from infrastructure not yet implemented.

## Documentation Changes

The ticket will update:

- `docs/04-technical-architecture.md`
- `docs/09-backend-architecture.md`
- `docs/10-testing-strategy.md`
- `docs/12-docker-deployment.md`
- `docs/14-implementation-notes.md`
- `docs/Repo_Current_State.md`

If `docs/12-docker-deployment.md` does not exist, it will be created and will describe only the intended environment-variable contract, not a completed Docker setup.

## Risks and Constraints

- Introducing too much profile structure now would pre-implement future deployment concerns.
- The configuration must not force a live database connection during test execution.
- Documentation must distinguish between configured properties and a runnable database environment.

## Completion Criteria

The ticket is complete when:

1. PostgreSQL dependency and datasource properties are present;
2. local profile configuration resolves Docker-compatible environment variable names;
3. automated tests verify the configuration baseline without a live database;
4. backend test verification passes fresh;
5. documentation reflects the new configuration accurately.
