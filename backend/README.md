# Backend

This module hosts the Spring Boot backend for the Insurance Claims Management System.

## Current scope

The backend now provides the completed Phase 1 foundation, the authentication phase, Phase 3 admin management APIs, and the Phase 4 claims workflow backend:

- Spring Boot scaffold and Java 21 / Maven baseline
- PostgreSQL configuration through the `local` profile
- Flyway migrations for core schema and auth seed data
- JPA entities, repositories, DTO mapping, and shared exception handling
- Spring Security with stateless JWT authentication
- `POST /api/auth/login`
- protected auth verification endpoints for `ADMIN`, `AGENT`, and `CLIENT`
- admin management endpoints for users, clients, and contracts
- client claim endpoints for draft creation, document metadata, submission, and self-service lookup
- agent/admin claim endpoints for review, approval, rejection, payment, and filtered listing
- OpenAPI JSON at `/v3/api-docs`
- Swagger UI at `/swagger-ui/index.html`

Local PostgreSQL connection properties are prepared through the `local` Spring profile in `src/main/resources/application-local.yml`.

## Build

On Windows:

```powershell
.\mvnw.cmd clean test
```

On Unix-like systems:

```bash
./mvnw clean test
```
