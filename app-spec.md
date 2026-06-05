# Insurance Claims Management System

## 1. Project Overview

The **Insurance Claims Management System** is a full-stack web application for managing health insurance claims from submission to final reimbursement decision.

The goal of this project is to demonstrate practical experience with a modern enterprise Java stack:

* Spring Boot backend
* Angular frontend
* REST API design
* JWT authentication
* Role-based access control
* SQL database modeling
* Dockerized deployment
* API documentation
* Testing
* CI/CD
* Technical documentation

This project is designed as a public GitHub reference implementation with detailed end-to-end documentation covering business requirements, architecture, implementation decisions, API behavior, database design, testing, deployment, and operational notes.

---

## 2. Business Context

In health insurance, clients submit claims after receiving medical services. These claims must be reviewed by insurance agents, validated against contract rules, approved or rejected, and eventually reimbursed.

This application simulates that workflow.

### Main users

| Role   | Description                                                                     |
| ------ | ------------------------------------------------------------------------------- |
| Admin  | Manages users, contracts, claim categories, and system settings                 |
| Agent  | Reviews claims, changes claim status, adds comments, approves or rejects claims |
| Client | Creates claims, uploads claim documents, tracks claim status                    |

---

## 3. Main Objectives

The project must showcase the ability to:

* Analyze business needs and translate them into technical features
* Design a clean backend architecture with Spring Boot
* Build a modular Angular frontend
* Design and query a relational database
* Secure APIs using JWT and role-based permissions
* Document technical decisions clearly
* Write tests for backend and frontend logic
* Prepare the application for deployment using Docker
* Use GitHub as a professional project showcase

---

## 4. Tech Stack

### Backend

| Technology        | Purpose                             |
|-------------------| ----------------------------------- |
| Java 21           | Main backend language               |
| Spring Boot 4     | Backend application framework       |
| Spring Web        | REST API development                |
| Spring Security   | Authentication and authorization    |
| Spring Data JPA   | ORM and database access             |
| Hibernate         | JPA implementation                  |
| Maven             | Dependency and build management     |
| PostgreSQL        | Relational database                 |
| Flyway            | Database migrations                 |
| Swagger / OpenAPI | API documentation                   |
| JUnit 5           | Unit and integration testing        |
| Mockito           | Mocking dependencies in tests       |
| Testcontainers    | Integration testing with PostgreSQL |
| Lombok            | Boilerplate reduction               |
| MapStruct         | DTO/entity mapping                  |

### Frontend

| Technology             | Purpose                           |
| ---------------------- | --------------------------------- |
| Angular 15+            | Frontend framework                |
| TypeScript             | Frontend language                 |
| Angular Router         | Page navigation                   |
| Angular Reactive Forms | Form handling and validation      |
| Angular Material       | UI components                     |
| RxJS                   | Async data handling               |
| JWT Interceptor        | Attach auth token to API requests |
| Jasmine / Karma        | Unit testing                      |
| Cypress or Playwright  | End-to-end testing                |

### DevOps and Quality

| Technology                  | Purpose                     |
| --------------------------- | --------------------------- |
| Git                         | Version control             |
| GitHub                      | Repository hosting          |
| Docker                      | Containerization            |
| Docker Compose              | Local multi-container setup |
| GitHub Actions or GitLab CI | CI/CD pipeline              |
| SonarQube or SonarCloud     | Code quality analysis       |
| Checkstyle                  | Java code style             |
| ESLint                      | Angular linting             |

### Optional Advanced Stack

| Technology    | Purpose                              |
| ------------- | ------------------------------------ |
| Kafka         | Claim status event publishing        |
| Elasticsearch | Advanced claim search                |
| Redis         | Caching frequent lookups             |
| OpenShift     | Deployment target simulation         |
| Camunda       | Workflow engine for claim processing |

---

## 5. Core Features

## 5.1 Authentication and Authorization

### Features

* User registration by admin
* Login with email and password
* JWT-based authentication
* Role-based access control
* Password hashing using BCrypt
* Protected Angular routes
* Backend endpoint authorization

### Roles

| Role   | Permissions                 |
| ------ | --------------------------- |
| Admin  | Full access                 |
| Agent  | Review and process claims   |
| Client | Submit and track own claims |

### Example secured endpoints

| Endpoint                        | Admin | Agent | Client |
| ------------------------------- | ----: | ----: | -----: |
| `GET /api/users`                |   Yes |    No |     No |
| `POST /api/claims`              |    No |    No |    Yes |
| `GET /api/claims`               |   Yes |   Yes |     No |
| `GET /api/claims/my`            |    No |    No |    Yes |
| `PATCH /api/claims/{id}/status` |   Yes |   Yes |     No |

---

## 5.2 Client Management

### Features

* Create client profile
* View client details
* Update client information
* Search clients by name, email, or ID
* View client contracts
* View client claims

### Main fields

* First name
* Last name
* Email
* Phone number
* Address
* Date of birth
* CIN / National ID
* Registration date

---

## 5.3 Contract Management

### Features

* Create insurance contract
* Assign contract to a client
* Define start date and end date
* Define coverage ceiling
* Define reimbursement rate
* Activate or deactivate contract
* View contract claim history

### Example contract fields

| Field              | Example          |
| ------------------ | ---------------- |
| Contract number    | `CN-2026-0001`   |
| Client             | Iyed Becheikh    |
| Type               | Health Insurance |
| Start date         | `2026-01-01`     |
| End date           | `2026-12-31`     |
| Coverage limit     | `5000 TND`       |
| Reimbursement rate | `80%`            |
| Status             | Active           |

---

## 5.4 Claim Management

### Features

* Client creates a claim
* Client uploads supporting documents
* Agent reviews claim
* Agent approves or rejects claim
* Agent adds review comments
* System calculates estimated reimbursement
* Client tracks claim status
* Admin views all claims

### Claim workflow

```text
DRAFT → SUBMITTED → UNDER_REVIEW → APPROVED → PAID
                         ↓
                      REJECTED
```

### Claim statuses

| Status       | Description                         |
| ------------ | ----------------------------------- |
| DRAFT        | Claim created but not submitted     |
| SUBMITTED    | Claim submitted by client           |
| UNDER_REVIEW | Claim is being reviewed by an agent |
| APPROVED     | Claim approved for reimbursement    |
| REJECTED     | Claim rejected                      |
| PAID         | Reimbursement completed             |

---

## 5.5 Reimbursement Calculation

### Formula

```text
reimbursementAmount = min(claimAmount × reimbursementRate, remainingCoverage)
```

### Example

```text
Claim amount: 1000 TND
Contract reimbursement rate: 80%
Remaining coverage: 600 TND

Calculated reimbursement:
1000 × 0.8 = 800 TND

Final reimbursement:
min(800, 600) = 600 TND
```

### Business rules

* Claim amount must be greater than 0
* Client must have an active contract
* Contract must not be expired
* Reimbursement cannot exceed remaining coverage
* Rejected claims do not consume coverage
* Paid claims consume available coverage

---

## 5.6 Document Upload

### Features

* Upload invoices, prescriptions, medical reports
* Store file metadata in database
* Store files locally or in object storage
* Validate file type
* Validate file size
* Download claim documents

### Supported file types

* PDF
* PNG
* JPG
* JPEG

### File metadata

| Field       | Description                |
| ----------- | -------------------------- |
| File name   | Original file name         |
| File type   | MIME type                  |
| Size        | File size                  |
| Upload date | Timestamp                  |
| Claim ID    | Related claim              |
| Uploaded by | User who uploaded the file |

---

## 5.7 Dashboard

### Admin dashboard

* Total clients
* Active contracts
* Total claims
* Claims by status
* Monthly claim amount
* Approved vs rejected claims

### Agent dashboard

* Claims waiting for review
* Claims reviewed this month
* Average processing time
* Recently updated claims

### Client dashboard

* My active contracts
* My submitted claims
* My approved reimbursements
* My rejected claims

---

## 6. Architecture

## 6.1 Backend Architecture

Use a layered Spring Boot architecture.

```text
controller
   ↓
service
   ↓
repository
   ↓
database
```

### Backend packages

```text
com.iyed.insuranceclaims
├── auth
│   ├── controller
│   ├── dto
│   ├── security
│   └── service
├── user
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── mapper
│   ├── repository
│   └── service
├── client
├── contract
├── claim
├── document
├── dashboard
├── common
│   ├── exception
│   ├── response
│   └── validation
└── config
```

### Backend design principles

* Controllers handle HTTP requests only
* Services contain business logic
* Repositories handle database access
* DTOs are used for API input/output
* Entities are not exposed directly
* Exceptions are handled globally
* Mappers convert between DTOs and entities
* Business rules are covered by tests

---

## 6.2 Frontend Architecture

Use a modular Angular architecture.

```text
src/app
├── core
│   ├── auth
│   ├── guards
│   ├── interceptors
│   └── services
├── shared
│   ├── components
│   ├── pipes
│   └── models
├── features
│   ├── auth
│   ├── dashboard
│   ├── clients
│   ├── contracts
│   ├── claims
│   └── users
└── layout
```

### Angular design principles

* Feature-based modules
* Reusable shared components
* Route guards for protected pages
* HTTP interceptor for JWT token
* Reactive forms for validation
* Services for API calls
* Models/interfaces for type safety

---

## 7. Database Design

## 7.1 Main Tables

### users

| Column        | Type      | Notes                |
| ------------- | --------- | -------------------- |
| id            | UUID      | Primary key          |
| email         | VARCHAR   | Unique               |
| password_hash | VARCHAR   | Hashed password      |
| role          | VARCHAR   | ADMIN, AGENT, CLIENT |
| enabled       | BOOLEAN   | Account status       |
| created_at    | TIMESTAMP | Creation date        |

### clients

| Column        | Type    | Notes       |
| ------------- | ------- | ----------- |
| id            | UUID    | Primary key |
| user_id       | UUID    | FK to users |
| first_name    | VARCHAR | Required    |
| last_name     | VARCHAR | Required    |
| phone         | VARCHAR | Required    |
| address       | VARCHAR | Optional    |
| national_id   | VARCHAR | Unique      |
| date_of_birth | DATE    | Required    |

### contracts

| Column             | Type    | Notes                      |
| ------------------ | ------- | -------------------------- |
| id                 | UUID    | Primary key                |
| client_id          | UUID    | FK to clients              |
| contract_number    | VARCHAR | Unique                     |
| type               | VARCHAR | HEALTH                     |
| start_date         | DATE    | Required                   |
| end_date           | DATE    | Required                   |
| coverage_limit     | DECIMAL | Required                   |
| reimbursement_rate | DECIMAL | Required                   |
| status             | VARCHAR | ACTIVE, EXPIRED, SUSPENDED |

### claims

| Column               | Type      | Notes           |
| -------------------- | --------- | --------------- |
| id                   | UUID      | Primary key     |
| client_id            | UUID      | FK to clients   |
| contract_id          | UUID      | FK to contracts |
| claim_number         | VARCHAR   | Unique          |
| claim_amount         | DECIMAL   | Required        |
| reimbursement_amount | DECIMAL   | Calculated      |
| status               | VARCHAR   | Workflow status |
| description          | TEXT      | Optional        |
| medical_service_date | DATE      | Required        |
| submitted_at         | TIMESTAMP | Nullable        |
| reviewed_at          | TIMESTAMP | Nullable        |
| reviewed_by          | UUID      | FK to users     |

### claim_documents

| Column      | Type      | Notes        |
| ----------- | --------- | ------------ |
| id          | UUID      | Primary key  |
| claim_id    | UUID      | FK to claims |
| file_name   | VARCHAR   | Required     |
| file_type   | VARCHAR   | Required     |
| file_path   | VARCHAR   | Required     |
| file_size   | BIGINT    | Required     |
| uploaded_at | TIMESTAMP | Required     |

### claim_comments

| Column     | Type      | Notes        |
| ---------- | --------- | ------------ |
| id         | UUID      | Primary key  |
| claim_id   | UUID      | FK to claims |
| author_id  | UUID      | FK to users  |
| comment    | TEXT      | Required     |
| created_at | TIMESTAMP | Required     |

---

## 8. REST API Specification

## 8.1 Authentication

### Login

```http
POST /api/auth/login
```

Request:

```json
{
  "email": "client@example.com",
  "password": "password123"
}
```

Response:

```json
{
  "accessToken": "jwt-token",
  "tokenType": "Bearer",
  "role": "CLIENT",
  "expiresIn": 3600
}
```

---

## 8.2 Claims

### Create claim

```http
POST /api/claims
Authorization: Bearer <token>
```

Request:

```json
{
  "contractId": "uuid",
  "claimAmount": 450.000,
  "medicalServiceDate": "2026-05-20",
  "description": "Medical consultation and prescribed medication"
}
```

Response:

```json
{
  "id": "uuid",
  "claimNumber": "CLM-2026-0001",
  "status": "DRAFT",
  "claimAmount": 450.000,
  "estimatedReimbursement": 360.000
}
```

### Submit claim

```http
PATCH /api/claims/{id}/submit
```

### Review claim

```http
PATCH /api/claims/{id}/status
```

Request:

```json
{
  "status": "APPROVED",
  "comment": "Valid invoice and active contract."
}
```

### Get claim by ID

```http
GET /api/claims/{id}
```

### Get my claims

```http
GET /api/claims/my
```

### Search claims

```http
GET /api/claims?status=UNDER_REVIEW&clientName=iyed&page=0&size=10
```

---

## 8.3 Contracts

### Create contract

```http
POST /api/contracts
```

Request:

```json
{
  "clientId": "uuid",
  "contractNumber": "CN-2026-0001",
  "type": "HEALTH",
  "startDate": "2026-01-01",
  "endDate": "2026-12-31",
  "coverageLimit": 5000.000,
  "reimbursementRate": 0.80
}
```

### Get contracts by client

```http
GET /api/clients/{clientId}/contracts
```

---

## 8.4 Documents

### Upload claim document

```http
POST /api/claims/{claimId}/documents
Content-Type: multipart/form-data
```

Form data:

```text
file = invoice.pdf
```

### Download document

```http
GET /api/documents/{documentId}/download
```

---

## 9. Security Requirements

### Backend security

* Passwords must be hashed with BCrypt
* APIs must require JWT except login
* Users can only access authorized resources
* Clients can only see their own claims
* Agents can review claims but cannot manage admin settings
* Admins can manage users and contracts
* Invalid tokens return `401 Unauthorized`
* Forbidden actions return `403 Forbidden`

### Frontend security

* Store JWT in memory or local storage
* Attach JWT using HTTP interceptor
* Protect routes with Angular guards
* Hide UI actions based on role
* Redirect unauthorized users

---

## 10. Validation Rules

### Claim validation

* Claim amount is required
* Claim amount must be positive
* Medical service date is required
* Medical service date cannot be in the future
* Contract must be active
* Contract must belong to the authenticated client
* Claim cannot be submitted without at least one document

### Contract validation

* Start date must be before end date
* Coverage limit must be positive
* Reimbursement rate must be between 0 and 1
* Contract number must be unique

### User validation

* Email must be unique
* Password must have minimum length
* Role must be valid

---

## 11. Error Handling

Use a global exception handler.

### Standard error response

```json
{
  "timestamp": "2026-06-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Claim amount must be greater than zero",
  "path": "/api/claims"
}
```

### Common errors

| Error                 | HTTP Status |
| --------------------- | ----------: |
| Invalid login         |         401 |
| Access denied         |         403 |
| Resource not found    |         404 |
| Validation error      |         400 |
| Duplicate email       |         409 |
| Internal server error |         500 |

---

## 12. Testing Strategy

## 12.1 Backend Tests

### Unit tests

Test service-layer business logic.

Examples:

* `ClaimServiceTest`
* `ContractServiceTest`
* `AuthServiceTest`
* `ReimbursementCalculatorTest`

### Integration tests

Test API endpoints with PostgreSQL using Testcontainers.

Examples:

* Login endpoint
* Create claim endpoint
* Submit claim endpoint
* Approve claim endpoint
* Access control rules

### Example test cases

| Test                                       | Expected result         |
| ------------------------------------------ | ----------------------- |
| Client creates claim with active contract  | Claim is created        |
| Client creates claim with expired contract | Error is returned       |
| Agent approves valid claim                 | Status becomes APPROVED |
| Client tries to approve claim              | 403 Forbidden           |
| Claim reimbursement exceeds coverage       | Reimbursement is capped |

---

## 12.2 Frontend Tests

### Unit tests

* Login form validation
* Claim form validation
* Auth guard behavior
* Claims service API calls
* Dashboard component rendering

### End-to-end tests

* User login
* Client submits claim
* Agent reviews claim
* Admin creates contract

---

## 13. CI/CD Pipeline

Use GitHub Actions.

### Pipeline steps

```text
1. Checkout repository
2. Build backend
3. Run backend tests
4. Run backend lint/checkstyle
5. Build Angular frontend
6. Run frontend tests
7. Run frontend lint
8. Build Docker images
9. Optional: run SonarCloud analysis
```

### Example pipeline file

```yaml
name: CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  backend:
    runs-on: ubuntu-latest

    services:
      postgres:
        image: postgres:16
        env:
          POSTGRES_DB: claims_db
          POSTGRES_USER: claims_user
          POSTGRES_PASSWORD: claims_password
        ports:
          - 5432:5432

    steps:
      - uses: actions/checkout@v4

      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: 21

      - name: Build backend
        working-directory: backend
        run: mvn clean verify

  frontend:
    runs-on: ubuntu-latest

    steps:
      - uses: actions/checkout@v4

      - name: Set up Node
        uses: actions/setup-node@v4
        with:
          node-version: 20

      - name: Install dependencies
        working-directory: frontend
        run: npm ci

      - name: Build frontend
        working-directory: frontend
        run: npm run build

      - name: Run tests
        working-directory: frontend
        run: npm test -- --watch=false
```

---

## 14. Docker Setup

### Services

* Backend API
* Angular frontend
* PostgreSQL database

### Docker Compose example

```yaml
version: "3.9"

services:
  postgres:
    image: postgres:16
    container_name: claims-postgres
    environment:
      POSTGRES_DB: claims_db
      POSTGRES_USER: claims_user
      POSTGRES_PASSWORD: claims_password
    ports:
      - "5432:5432"
    volumes:
      - claims_postgres_data:/var/lib/postgresql/data

  backend:
    build: ./backend
    container_name: claims-backend
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/claims_db
      SPRING_DATASOURCE_USERNAME: claims_user
      SPRING_DATASOURCE_PASSWORD: claims_password
      JWT_SECRET: change-me
    ports:
      - "8080:8080"

  frontend:
    build: ./frontend
    container_name: claims-frontend
    depends_on:
      - backend
    ports:
      - "4200:80"

volumes:
  claims_postgres_data:
```

---

## 15. Documentation Plan

This is the most important part of the project. The repository should look professional and explain not only what was built, but why each technical choice was made.

## 15.1 Required Documentation Files

```text
docs/
├── 01-project-overview.md
├── 02-business-requirements.md
├── 03-functional-specification.md
├── 04-technical-architecture.md
├── 05-database-design.md
├── 06-api-documentation.md
├── 07-security-design.md
├── 08-frontend-architecture.md
├── 09-backend-architecture.md
├── 10-testing-strategy.md
├── 11-ci-cd.md
├── 12-docker-deployment.md
├── 13-troubleshooting.md
├── 14-implementation-notes.md
└── 15-roadmap.md
```

---

## 15.2 README.md Structure

The README should include:

```text
# Insurance Claims Management System

## Overview
Short explanation of the project.

## Why I Built This Project
Explain that the repository is a documented reference implementation of a full-stack insurance claims platform built with Java 21, Spring Boot 4, Angular, SQL, Docker, CI/CD, and enterprise application design practices.

## Tech Stack
Clear backend/frontend/devops table.

## Features
List core features.

## Architecture
Include simple architecture diagram.

## Screenshots
Add UI screenshots when available.

## Getting Started
Local setup instructions.

## Running with Docker
Docker Compose instructions.

## API Documentation
Link to Swagger UI.

## Testing
Explain backend and frontend tests.

## CI/CD
Explain GitHub Actions pipeline.

## Documentation
Link to docs folder.

## Roadmap
List future improvements.

## Author
Your name, LinkedIn, GitHub.
```

---

## 15.3 Architecture Documentation

The architecture documentation should explain:

* Why Spring Boot was used
* Why Angular was used
* Why PostgreSQL was selected
* Why DTOs are used instead of exposing entities
* Why layered architecture was selected
* How authentication works
* How authorization works
* How claim status workflow works
* How frontend communicates with backend
* How Docker Compose connects services

---

## 15.4 API Documentation

Use two levels of API documentation.

### Level 1: Swagger

Swagger should be available at:

```text
http://localhost:8080/swagger-ui/index.html
```

### Level 2: Markdown API documentation

Create `docs/06-api-documentation.md`.

For each endpoint, document:

* Purpose
* HTTP method
* URL
* Required role
* Request body
* Response body
* Validation errors
* Example curl request

Example:

````md
## Create Claim

### Purpose

Allows a client to create a new claim under an active insurance contract.

### Endpoint

POST /api/claims

### Required Role

CLIENT

### Request Body

```json
{
  "contractId": "uuid",
  "claimAmount": 450.000,
  "medicalServiceDate": "2026-05-20",
  "description": "Medical consultation"
}
````

### Response

```json
{
  "id": "uuid",
  "claimNumber": "CLM-2026-0001",
  "status": "DRAFT"
}
```

````

---

## 15.5 Implementation Notes Documentation

Create `docs/14-implementation-notes.md`.

This file should record non-obvious implementation decisions, trade-offs, limitations, and operational details discovered while building the system. It should be useful to future maintainers reading the public repository.

### Topics to document

#### Backend implementation notes

- Spring Boot 4 configuration decisions
- Package structure and module boundaries
- Entity relationship decisions
- DTO and mapper conventions
- Validation strategy
- Global exception handling conventions
- Security filter chain configuration
- JWT structure, expiration, and refresh approach if implemented
- File upload storage approach and limitations
- Reimbursement calculation edge cases

#### Frontend implementation notes

- Angular feature structure
- Route protection strategy
- HTTP interceptor behavior
- Form validation conventions
- Error display strategy
- Table, pagination, and filtering behavior
- Role-based UI rendering decisions

#### Database implementation notes

- Migration strategy
- Constraints and indexes
- Enum storage strategy
- Audit timestamp handling
- Seed data conventions

#### Testing implementation notes

- Unit test boundaries
- Integration test setup
- Testcontainers configuration
- Frontend test coverage decisions
- Known test limitations

#### Deployment implementation notes

- Docker image structure
- Docker Compose service communication
- Environment variables
- Local development setup
- CI pipeline limitations
- Known operational risks

---

## 16. GitHub Repository Structure

```text
insurance-claims-management-system/
├── backend/
│   ├── src/
│   ├── pom.xml
│   ├── Dockerfile
│   └── README.md
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── Dockerfile
│   └── README.md
├── docs/
│   ├── 01-project-overview.md
│   ├── 02-business-requirements.md
│   ├── 03-functional-specification.md
│   ├── 04-technical-architecture.md
│   ├── 05-database-design.md
│   ├── 06-api-documentation.md
│   ├── 07-security-design.md
│   ├── 08-frontend-architecture.md
│   ├── 09-backend-architecture.md
│   ├── 10-testing-strategy.md
│   ├── 11-ci-cd.md
│   ├── 12-docker-deployment.md
│   ├── 13-troubleshooting.md
│   ├── 14-implementation-notes.md
│   └── 15-roadmap.md
├── .github/
│   └── workflows/
│       └── ci.yml
├── docker-compose.yml
├── README.md
└── LICENSE
````

---

## 17. Suggested Development Phases

## Phase 1: Backend Foundation

* Create Spring Boot project
* Configure PostgreSQL
* Add Flyway
* Create entities
* Create repositories
* Add DTOs
* Add MapStruct
* Add global exception handler

## Phase 2: Authentication

* Add Spring Security
* Implement login
* Generate JWT
* Add role-based authorization
* Seed default users

## Phase 3: Claims Core

* Create contracts
* Create claims
* Submit claims
* Review claims
* Calculate reimbursement
* Add claim comments

## Phase 4: Angular Foundation

* Create Angular app
* Add routing
* Add Angular Material
* Add auth module
* Add login page
* Add JWT interceptor
* Add route guards

## Phase 5: Main UI

* Admin dashboard
* Agent dashboard
* Client dashboard
* Claims list
* Claim details
* Claim form
* Contract management

## Phase 6: Documentation and Testing

* Add Swagger
* Write API docs
* Add backend tests
* Add frontend tests
* Add screenshots
* Add implementation notes

## Phase 7: Docker and CI/CD

* Add Dockerfiles
* Add Docker Compose
* Add GitHub Actions
* Add SonarCloud or local SonarQube config
