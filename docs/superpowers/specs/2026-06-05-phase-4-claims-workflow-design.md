# Phase 4 Claims Workflow Design

## Scope

This phase delivers the backend claims workflow without dashboards or frontend work.

Included:

- client draft claim creation;
- client claim listing and detail retrieval for owned claims;
- claim document metadata persistence;
- claim submission validation;
- agent/admin claim listing and detail retrieval;
- agent/admin review actions and status transitions;
- ownership and role enforcement;
- reimbursement calculation using the existing contract model.

Excluded:

- dashboard endpoints;
- binary file storage or upload transport;
- notifications;
- PDF export;
- audit logging.

## API surface

### Client endpoints

- `POST /api/claims`
- `GET /api/claims/my`
- `GET /api/claims/my/{id}`
- `POST /api/claims/{id}/documents`
- `POST /api/claims/{id}/submit`

### Agent/admin endpoints

- `GET /api/claims`
- `GET /api/claims/{id}`
- `PATCH /api/claims/{id}/start-review`
- `PATCH /api/claims/{id}/approve`
- `PATCH /api/claims/{id}/reject`
- `PATCH /api/claims/{id}/pay`

## Core rules

- a client can create a claim only for their own client profile;
- the selected contract must belong to that client;
- the contract must be `ACTIVE`;
- the medical service date must fall inside the contract date range;
- claim amount must be greater than zero;
- a claim can be submitted only from `DRAFT`;
- a claim must have at least one document before submission;
- only `SUBMITTED` claims can move to `UNDER_REVIEW`;
- only `UNDER_REVIEW` claims can move to `APPROVED` or `REJECTED`;
- only `APPROVED` claims can move to `PAID`.

## Reimbursement behavior

The reimbursement formula remains:

`min(claimAmount * reimbursementRate, remainingCoverage)`

`remainingCoverage` is computed from the contract coverage limit minus the sum of reimbursement amounts for `PAID` claims on that contract.

Implementation choice:

- reimbursement amount is calculated on approval for immediate reviewer visibility;
- reimbursement amount is recalculated again on payment to ensure the final paid amount still respects current remaining coverage.

## Persistence approach

No schema migration is required for this phase because the existing `claims`, `claim_documents`, and related core tables already support the workflow.

New request DTOs, services, and controllers will be added around the current schema.

## Testing approach

The phase will be driven by integration tests covering:

- client draft creation and ownership;
- submission blocked without documents;
- document metadata persistence;
- agent/admin review transitions;
- reimbursement calculation and payment behavior;
- filtered claim listing for reviewers.
