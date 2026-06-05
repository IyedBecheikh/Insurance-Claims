# Frontend Architecture

## Stack

- Angular 20
- Angular Material
- Angular Router
- Reactive Forms
- HttpClient with JWT interceptor
- Karma + Jasmine for the current unit test layer

## Module structure

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
  features/
    auth/
    dashboard/
    users/
    clients/
    contracts/
    claims/
  layout/
```

## Core application flow

- `AuthStateService` stores the active session in `localStorage`.
- `AuthInterceptor` attaches the bearer token to API requests automatically.
- `authGuard` blocks anonymous access to the shell.
- `roleGuard` enforces per-route role restrictions.
- `EnvironmentService` centralizes the API base path as `/api`.

## Layout

The app uses a persistent left navigation drawer plus a top toolbar:

- left navigation is role-aware;
- the toolbar exposes the current role and signed-in identity;
- the primary working area stays focused on tables, forms, and workflow detail rather than decorative layout patterns.

## Feature screens

### Auth

- login page with seeded-development credentials prefilled for local exploration

### Dashboard

- one shared dashboard page that adapts to `ADMIN`, `AGENT`, or `CLIENT`
- metric cards and recent-claims summaries are derived from existing backend endpoints

### Users

- admin-only table view
- inline create form
- selected-user detail panel
- enable/disable actions

### Clients

- admin-only directory view
- create/edit form in the same workflow surface

### Contracts

- admin-only table view
- create/edit form
- activate/suspend actions

### Claims

- client page for draft creation, document metadata entry, and submission
- reviewer page for filtering, detail inspection, and workflow actions

## Shared UI components

- `StatusBadgeComponent` for claim and contract states
- `StatCardComponent` for dashboard metrics
- `SectionHeaderComponent` for consistent page framing

## API integration pattern

Each business area uses a focused API service:

- `UsersApiService`
- `ClientsApiService`
- `ContractsApiService`
- `ClaimsApiService`

These services mirror the backend resource structure instead of introducing an extra frontend abstraction layer too early.

## Current limitations

- binary document upload is not implemented because the backend currently accepts document metadata only
- client contract self-service is limited to the `/api/contracts/my` read surface needed for claim creation
- there is no dedicated frontend E2E suite yet
