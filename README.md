# Insurance Claims Management System

An enterprise-style reference implementation of a health-insurance claims workflow. It separates client submission, agent review, and administrative management into a Spring Boot API and Angular application.

> Status: reference implementation. Review security, deployment, and integration settings before using it in production.

## Highlights

- JWT authentication and role-based access for `ADMIN`, `AGENT`, and `CLIENT`
- Claim submission, review, contracts, clients, and user-management workflows
- Spring Boot 4 / Java 21 backend with PostgreSQL-oriented Flyway migrations
- Angular 20 / Angular Material frontend
- Docker Compose, Swagger/OpenAPI, and project documentation

## Screenshots

![Login screen](docs/assets/ui-login.png)

![Admin dashboard](docs/assets/ui-admin-dashboard.png)

## Run locally

The repository contains `docker-compose.yml`, a `backend/` Spring Boot service, and a `frontend/` Angular application.

```bash
docker compose up --build
```

For component-level development, configure the database and application settings first, then run the backend from `backend/` with Maven and the frontend from `frontend/` with:

```bash
npm install
npm start
```

See [app-spec.md](app-spec.md) for the implemented workflow and API scope.

## Roles

- `CLIENT` submits and tracks claims.
- `AGENT` reviews submitted claims and records decisions.
- `ADMIN` manages users, clients, contracts, and claim oversight.

## License

See [LICENSE](LICENSE).