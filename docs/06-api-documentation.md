# API Documentation

## Current status

The backend now exposes authentication, Phase 3 admin management endpoints, and the Phase 4 claim workflow endpoints under `/api`.

## Authentication endpoints

### `POST /api/auth/login`

Authenticates an enabled user and returns a signed JWT.

Request body:

```json
{
  "email": "admin@insurance.local",
  "password": "Password123!"
}
```

Response body:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "role": "ADMIN",
  "expiresIn": 3600
}
```

Notes:

- public endpoint;
- credentials are validated through Spring Security authentication manager;
- passwords are stored hashed with BCrypt.

### `GET /api/auth/me`

Returns the authenticated user identity resolved from the JWT.

Response body:

```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "email": "admin@insurance.local",
  "role": "ADMIN"
}
```

Authorization:

- any authenticated `ADMIN`, `AGENT`, or `CLIENT`.

### `GET /api/auth/admin-only`

Protected verification endpoint for `ADMIN`.

### `GET /api/auth/agent-only`

Protected verification endpoint for `AGENT`.

### `GET /api/auth/client-only`

Protected verification endpoint for `CLIENT`.

These three verification endpoints currently return the same authenticated-user payload as `/api/auth/me`. They remain useful as a narrow security verification surface alongside the business APIs.

## Admin management endpoints

All endpoints in this section require the `ADMIN` role.

### Users

- `POST /api/users`
- `GET /api/users`
- `GET /api/users/{id}`
- `PATCH /api/users/{id}/enabled?enabled=true|false`

Create-user request:

```json
{
  "email": "new.user@insurance.local",
  "password": "Password123!",
  "role": "CLIENT",
  "enabled": true
}
```

### Clients

- `POST /api/clients`
- `GET /api/clients`
- `GET /api/clients/{id}`
- `PUT /api/clients/{id}`

Create-client request:

```json
{
  "userId": "11111111-1111-1111-1111-111111111111",
  "firstName": "Jane",
  "lastName": "Doe",
  "phone": "+21612345678",
  "address": "Tunis",
  "nationalId": "CL-3001",
  "dateOfBirth": "1990-01-15"
}
```

### Contracts

- `POST /api/contracts`
- `GET /api/contracts`
- `GET /api/contracts/my`
- `GET /api/contracts/{id}`
- `PUT /api/contracts/{id}`
- `PATCH /api/contracts/{id}/activate`
- `PATCH /api/contracts/{id}/suspend`

Create-contract request:

```json
{
  "clientId": "44444444-4444-4444-4444-444444444444",
  "contractNumber": "CT-3001",
  "type": "HEALTH",
  "startDate": "2026-01-01",
  "endDate": "2026-12-31",
  "coverageLimit": 5000.00,
  "reimbursementRate": 0.80,
  "status": "ACTIVE"
}
```

`GET /api/contracts/my` returns the authenticated client's own contracts and is used by the Angular claim-creation flow.

## Claim workflow endpoints

### Client claim endpoints

These endpoints require the `CLIENT` role.

- `POST /api/claims`
- `GET /api/claims/my`
- `GET /api/claims/my/{id}`
- `POST /api/claims/{id}/documents`
- `POST /api/claims/{id}/submit`

Create-claim request:

```json
{
  "contractId": "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa",
  "serviceDate": "2026-03-15",
  "claimAmount": 450.00,
  "description": "Outpatient consultation"
}
```

Add-document request:

```json
{
  "fileName": "invoice.pdf",
  "contentType": "application/pdf",
  "storagePath": "claims/CLM-12345678/invoice.pdf"
}
```

Submit rules:

- the claim must belong to the authenticated client;
- the claim must still be in `DRAFT`;
- at least one document must exist.

### Reviewer claim endpoints

These endpoints require `AGENT` or `ADMIN`.

- `GET /api/claims`
- `GET /api/claims/{id}`
- `PATCH /api/claims/{id}/start-review`
- `PATCH /api/claims/{id}/approve`
- `PATCH /api/claims/{id}/reject`
- `PATCH /api/claims/{id}/pay`

Supported query parameters for `GET /api/claims`:

- `status`
- `clientId`
- `claimNumber`

Approval behavior:

- reimbursement is calculated from claim amount, contract reimbursement rate, and remaining contract coverage;
- approved claims receive a reimbursement amount;
- paying a claim finalizes its coverage consumption logically through the contract's paid-claim history.

## Established response boundary conventions

Core aggregate response DTOs already exist for:

- user
- client
- contract
- claim
- claim document
- claim comment

These DTOs remain the required boundary for future business endpoints so JPA entities are not exposed directly.

## Standard error response

The shared API error model follows this structure:

```json
{
  "timestamp": "2026-06-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Claim amount must be greater than zero",
  "path": "/api/claims"
}
```

This contract is implemented centrally through the global exception handler and the security entry-point / access-denied handlers.

Common auth-related cases:

- `401 Unauthorized` for missing, invalid, or expired JWTs;
- `401 Unauthorized` for invalid login credentials;
- `403 Forbidden` for authenticated users lacking the required role.
- `404 Not Found` for unknown users, clients, contracts, or claims;
- `409 Conflict` for duplicate user emails, duplicate client profiles per user, or duplicate contract numbers.
