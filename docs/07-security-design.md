# Security Design

## Current authentication model

The backend now uses stateless JWT authentication with Spring Security.

Supported roles:

- `ADMIN`
- `AGENT`
- `CLIENT`

## Login flow

1. A client sends credentials to `POST /api/auth/login`.
2. Spring Security authenticates the user through the configured `AuthenticationManager`.
3. Password verification uses BCrypt against the stored `password_hash`.
4. A signed JWT is returned to the caller on success.

## JWT design

- token type: bearer token;
- signing approach: HMAC secret from configuration;
- subject claim: user email;
- custom role claim: `role`;
- issued-at and expiration timestamps are included;
- token lifetime is controlled by `security.jwt.expiration-ms`.

The JWT secret must be supplied through configuration or environment variables in real deployments. The repository keeps only development/test defaults.

## Request security model

- security is stateless;
- CSRF is disabled for the current token-based API design;
- `/api/auth/login` is public;
- `/api/auth/me` requires authentication;
- `/api/auth/admin-only` requires `ADMIN`;
- `/api/auth/agent-only` requires `AGENT`;
- `/api/auth/client-only` requires `CLIENT`;
- `/api/users/**` requires `ADMIN`;
- `/api/clients/**` requires `ADMIN`;
- `/api/contracts/my` requires `CLIENT`;
- `/api/contracts/**` requires `ADMIN`;
- `/api/claims/my/**` and client document/submission actions require `CLIENT`;
- reviewer claim listing, detail, and status actions require `ADMIN` or `AGENT`;
- `/v3/api-docs` and Swagger UI remain public for documentation access;
- all other routes default to authenticated access unless later tickets open them explicitly.

## Seeded development users

The second Flyway migration seeds one enabled user per role:

- `admin@insurance.local`
- `agent@insurance.local`
- `client@insurance.local`

Each seeded user currently uses the shared development password `Password123!`. This is intentional for local verification only and must not be treated as production practice.

The client user is linked to a seeded `clients` record so later client-scoped workflows can build on a real persisted relationship.

## Error handling

Security failures return the shared API error shape:

- unauthenticated requests return `401`;
- invalid credentials return `401`;
- insufficient-role requests return `403`.

## Ownership model

- clients can only create and access claims tied to their own client profile;
- clients cannot access claims belonging to another client;
- reviewers can access all claims exposed by the current workflow endpoints;
- contract ownership is enforced during client claim creation.

## Current limitations

- no refresh-token flow;
- no registration or password reset;
- no binary file-upload security yet, only persisted document metadata;
- no externalized secret-management or deployment hardening yet.
