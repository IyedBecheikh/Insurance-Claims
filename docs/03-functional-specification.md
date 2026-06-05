# Functional Specification

## Phase 3 and Phase 4 scope

The backend now exposes the admin management surface plus the first full claim workflow surface.

## Admin user management

Administrators can:

- create users;
- list users;
- view a user by identifier;
- enable or disable a user account.

## Admin client management

Administrators can:

- create a client profile for an existing `CLIENT` user;
- list clients;
- view a client by identifier;
- update client profile details.

## Admin contract management

Administrators can:

- create contracts for clients;
- list contracts;
- view a contract by identifier;
- update contract details;
- suspend a contract;
- reactivate a suspended contract.

## API discoverability

The backend exposes OpenAPI documentation and Swagger UI so the current auth and admin management endpoints can be explored directly from the running application.

## Client claim workflow

Authenticated clients can:

- create draft claims for their own active contracts;
- list their own claims;
- view their own claim details;
- attach document metadata to their own draft claims;
- submit their own draft claims once at least one document exists.

## Reviewer claim workflow

Authenticated `AGENT` and `ADMIN` users can:

- list claims with optional filters by status, client, and claim number;
- view claim details;
- move submitted claims into `UNDER_REVIEW`;
- approve or reject claims under review;
- mark approved claims as paid.
