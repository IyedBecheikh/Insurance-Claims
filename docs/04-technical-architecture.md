# Technical Architecture

## Current baseline

The repository is organized as a multi-part application with separate `backend`, `frontend`, and `docs` directories. This separation is intentional and matches the public portfolio structure defined in `AGENTS.md`.

## Backend approach

The backend uses a layered Spring Boot architecture and a feature-oriented package layout rooted at `com.iyed.insuranceclaims`. The current scaffold prepares the package boundaries without implementing database, security, or workflow logic ahead of schedule.

The configuration baseline now separates shared settings from local PostgreSQL settings by using a `local` Spring profile. This keeps default application configuration small while preparing the backend for local database work in later tickets.

## Deferred architecture

Flyway, Spring Security, JWT, Angular integration, Docker runtime wiring, and CI automation remain deferred to later tickets so the project history stays aligned with the one-ticket-per-run rule.
