# Phase 5 Complete UI Design

Date: 2026-06-05

## Goal

Deliver the full Angular frontend for the current backend scope:

- login
- role-based dashboards
- admin management for users, clients, and contracts
- client claim workflow
- reviewer claim workflow
- portfolio-ready README updates and screenshots

This phase does not add new backend endpoints. The UI must stay honest to the current API surface.

## Chosen approach

Use Angular with Angular Material and a route-first feature structure.

Why:

- it fits the internal back-office style required by the project;
- it provides mature table, form, dialog, drawer, and feedback components quickly;
- it keeps the app maintainable as more backend phases land.

## Constraints and assumptions

- JWT auth remains the only authentication mechanism.
- Document handling is metadata-only because the backend does not support binary upload yet.
- Dashboard views use existing API calls and local derived summaries rather than a dedicated dashboard API.
- Client-facing contract display must be limited to data available through the current backend. The UI must not fabricate unsupported self-service endpoints.

## Frontend architecture

Create `docs/08-frontend-architecture.md` and implement the Angular app under `frontend/` with this structure:

```text
src/app/
  core/
    auth/
    guards/
    interceptors/
    services/
  shared/
    components/
    models/
    pipes/
  features/
    auth/
    dashboard/
    users/
    clients/
    contracts/
    claims/
  layout/
```

Key pieces:

- auth state service backed by `localStorage`
- JWT interceptor
- route guard plus role-aware route metadata
- shared API error parsing against the backend error shape
- reusable data-table and status-chip presentation components where it reduces duplication

## UI scope

### Authentication

- centered login page
- redirect by role after login
- logout from global shell

### Layout

- persistent app shell with top bar and sidenav
- role-aware navigation
- compact, professional light theme

### Dashboards

- admin: quick counts and shortcuts for users, clients, contracts, and claim backlog
- agent: claim review queue emphasis
- client: claim activity emphasis plus contract/claim guidance based on available data

### Admin features

- user list, create form, detail view, enable/disable actions
- client list, create form, edit form, detail view
- contract list, create form, edit form, detail view, activate/suspend actions

### Claims features

- client: list own claims, create draft, view detail, add document metadata, submit
- reviewer: list claims with status/client/claim-number filters, detail view, start review, approve, reject, pay

## Testing strategy

Use frontend test-first for core UI behavior:

- auth state and role redirection
- route guard behavior
- key service calls
- at least one feature component test per major area where behavior is meaningful

Verification for the phase:

- `npm install`
- `npm run build`
- `npm test -- --watch=false --browsers=ChromeHeadless`
- backend `.\mvnw.cmd test`

## Visual direction

Use a restrained operational UI:

- light neutral surfaces
- clear typography
- dense but readable tables
- strong status colors for claim and contract lifecycle states
- minimal decorative treatment

The memorable part should be the clarity of the workflow surfaces, not ornamental chrome.

## Screenshots and README

Generate screenshots under `docs/assets/` for:

- login
- admin dashboard
- user management
- contract management
- client claim creation/detail
- reviewer claims queue/detail

Update `README.md` to be GitHub-ready with:

- project overview
- stack
- implemented features by role
- local run/test instructions
- backend/frontend docs links
- screenshots section
- roadmap summary

## Out of scope

- binary document upload
- charts library unless materially needed for dashboard clarity
- Docker runtime setup
- CI/CD setup
