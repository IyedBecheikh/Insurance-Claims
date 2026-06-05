# Database Design

## Current status

The backend foundation now includes a first Flyway migration that creates the six core tables:

- `users`
- `clients`
- `contracts`
- `claims`
- `claim_documents`
- `claim_comments`

## Primary key strategy

All core tables use UUID primary keys. This keeps identifier generation decoupled from database sequences and aligns with the repository rules defined in `AGENTS.md`.

## Core relationships

- `clients.user_id -> users.id`
- `contracts.client_id -> clients.id`
- `claims.client_id -> clients.id`
- `claims.contract_id -> contracts.id`
- `claims.reviewed_by -> users.id`
- `claim_documents.claim_id -> claims.id`
- `claim_comments.claim_id -> claims.id`
- `claim_comments.author_id -> users.id`

## Constraints

The migration enforces:

- unique email, national ID, contract number, and claim number values;
- positive coverage and claim amounts;
- reimbursement rate bounded between `0` and `1`;
- string-backed enum value checks for roles, contract status, contract type, and claim status.

## Current claim persistence usage

The existing schema was sufficient for the first claim workflow phase, so no new migration was needed.

- `claims.reviewed_by` and `claims.reviewed_at` are now populated by reviewer actions.
- `claims.reimbursement_amount` is assigned when a claim is approved.
- `claim_documents` now persists document metadata for draft claim submissions.

Coverage consumption is derived from paid claims tied to the same contract rather than from a separate accounting table at this stage.

## Migration approach

Schema changes are now managed through Flyway under `backend/src/main/resources/db/migration`. Hibernate auto-DDL remains disabled as a committed schema-management strategy.
