# Business Requirements

## Roles

- `ADMIN` manages users, client profiles, and contracts.
- `AGENT` reviews submitted claims and records claim decisions.
- `CLIENT` submits and tracks their own claims.

## Admin management requirements

### Users

- Admins can create users for all supported roles.
- User email addresses must be unique.
- Passwords must be stored as BCrypt hashes.
- Admins can enable or disable users without deleting them.

### Clients

- A client profile must be linked to exactly one existing `CLIENT` user.
- A non-client user cannot own a client profile.
- A user can have at most one client profile.
- Client national identifiers must remain unique.

### Contracts

- A contract must be linked to an existing client profile.
- Contract numbers must remain unique.
- Coverage limit must be greater than zero.
- Reimbursement rate must stay between `0` and `1`.
- Contract end date must not be earlier than the start date.
- Admins can suspend or reactivate contracts through dedicated status actions.
- Expired contracts are not reactivated through the current Phase 3 API.

## Claim workflow requirements

- A client can create a claim only for their own contract.
- A claim can be created only for an `ACTIVE` contract.
- The medical service date must fall inside the contract date range.
- Claim amount must be greater than zero.
- A new client claim starts in `DRAFT`.
- A draft claim must have at least one document before submission.
- A client can submit only their own draft claim.
- Only `SUBMITTED` claims can move to `UNDER_REVIEW`.
- Only `UNDER_REVIEW` claims can be approved or rejected.
- Only `APPROVED` claims can move to `PAID`.
- `AGENT` and `ADMIN` can review claims.
- A rejected claim does not consume contract coverage.
- A paid claim consumes contract coverage through the approved reimbursement amount.

## Reimbursement rules

- Reimbursement is calculated as `min(claimAmount * reimbursementRate, remainingCoverage)`.
- Remaining coverage is the contract coverage limit minus reimbursement amounts already paid for the same contract.
- Reimbursement is assigned when the claim is approved.
- Marking a claim as `PAID` finalizes the coverage consumption already represented by the reimbursement amount.
