# Frontend

Angular 20 + Angular Material frontend for the Insurance Claims Management System.

## Current scope

- login page with JWT session storage
- role-aware shell and navigation
- dashboards for admin, agent, and client roles
- admin screens for users, clients, and contracts
- client claim workflow screens
- reviewer claim queue and decision screens

## Development

Install dependencies:

```powershell
npm install
```

Start the development server:

```powershell
npm start
```

The dev server proxies `/api` requests to `http://localhost:8080` through `proxy.conf.json`.

## Build

```powershell
npm run build
```

## Tests

```powershell
npx ng test --watch false --browsers ChromeHeadless --progress=false
```

## Structure

```text
src/app/
  core/
  shared/
  features/
  layout/
```
