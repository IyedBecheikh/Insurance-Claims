# Authentication Phase Completion Design

## Objective

Complete the authentication phase as a single user-approved subphase. This phase will add Spring Security, login, JWT issuance and validation, role-based authorization, and seeded default users for all three roles without extending into broader user management or frontend implementation.

## Scope

This subphase includes:

- adding Spring Security dependencies and configuration;
- adding JWT generation and validation;
- implementing `POST /api/auth/login`;
- adding a small authenticated verification surface:
  - `GET /api/auth/me`
  - `GET /api/auth/admin-only`
  - `GET /api/auth/agent-only`
  - `GET /api/auth/client-only`
- enforcing role-based authorization for those endpoints;
- adding Flyway seed data for default `ADMIN`, `AGENT`, and `CLIENT` users;
- storing seeded passwords as BCrypt hashes;
- adding backend tests for login and authorization behavior;
- updating backend, security, API, testing, implementation-note, and repo-state documentation.

This subphase excludes:

- user registration flows;
- refresh tokens;
- logout token revocation;
- password reset;
- frontend login UI;
- claims/contract ownership authorization rules beyond simple role checks;
- Docker runtime completion;
- CI/CD completion.

## Recommended Approach

Use a minimal stateless authentication model:

1. authenticate by email and password;
2. return a signed JWT with the user role;
3. validate that JWT on subsequent requests through a security filter;
4. protect endpoints with role-based rules;
5. seed three enabled users so all roles can log in immediately.

This keeps the auth phase small enough to verify while still producing a realistic enterprise backend foundation.

## Dependencies

The backend Maven build will add:

- `spring-boot-starter-security`
- `io.jsonwebtoken:jjwt-api`
- `io.jsonwebtoken:jjwt-impl`
- `io.jsonwebtoken:jjwt-jackson`
- `spring-security-test`

## Data and Seeding

A new Flyway migration will seed:

- one enabled `ADMIN` user;
- one enabled `AGENT` user;
- one enabled `CLIENT` user;
- one `clients` row tied to the client user.

Seeded passwords will be stored as BCrypt hashes. No plaintext credentials will be committed into the schema as data values outside documented sample credentials.

## Security Model

### Authentication

- `POST /api/auth/login` is public.
- Credentials are validated against the seeded users and later persisted users.
- Successful login returns a bearer token, role, and expiry metadata.

### Authorization

- Security is stateless.
- All endpoints are authenticated by default unless explicitly public.
- Role checks are enforced for the verification endpoints:
  - admin endpoint requires `ADMIN`
  - agent endpoint requires `AGENT`
  - client endpoint requires `CLIENT`

### JWT

The token will contain:

- `sub`: user email
- `role`: user role
- issued-at timestamp
- expiration timestamp

The JWT secret and expiration will be configuration-driven rather than hardcoded.

## Backend Components

Planned auth components:

- `LoginRequestDto`
- `LoginResponseDto`
- `AuthenticatedUserResponseDto`
- `JwtService`
- `CustomUserDetailsService`
- JWT authentication filter
- `SecurityConfig`
- `AuthService`
- `AuthController`

The existing exception infrastructure will be reused for predictable auth failures where appropriate.

## Testing

The subphase will add tests for:

- successful login for seeded users;
- login failure with invalid password;
- access denied without token;
- access denied for valid token with wrong role;
- access granted for valid token with correct role;
- token parsing/validation behavior at unit level where practical.

Tests should verify real security behavior, not only controller wiring.

## Documentation Changes

The subphase will create or update:

- `docs/06-api-documentation.md`
- `docs/07-security-design.md`
- `docs/09-backend-architecture.md`
- `docs/10-testing-strategy.md`
- `docs/14-implementation-notes.md`
- `docs/Repo_Current_State.md`

Documentation will state clearly that this phase enables authentication and role checks, but not full business authorization rules for claims and contracts yet.

## Risks and Constraints

- JWT secret handling must stay configuration-driven and not be committed as a production-style secret.
- Seed data must remain realistic but minimal.
- Role-based checks at this phase prove security wiring only; ownership-based authorization remains for later domain phases.
- The verification endpoints should stay narrow so they do not turn into accidental long-term business APIs.

## Completion Criteria

This auth subphase is complete when:

1. Spring Security and JWT-based stateless auth are wired in;
2. seeded `ADMIN`, `AGENT`, and `CLIENT` users can log in successfully;
3. protected verification endpoints enforce authentication and role checks;
4. backend auth/security tests pass fresh;
5. documentation reflects the actual auth capabilities and limitations accurately.
