create table users (
    id uuid primary key,
    email varchar(255) not null unique,
    password_hash varchar(255) not null,
    role varchar(20) not null check (role in ('ADMIN', 'AGENT', 'CLIENT')),
    enabled boolean not null,
    created_at timestamp not null default current_timestamp
);

create table clients (
    id uuid primary key,
    user_id uuid not null unique references users(id),
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    phone varchar(50) not null,
    address varchar(255),
    national_id varchar(50) not null unique,
    date_of_birth date not null,
    registration_date timestamp not null default current_timestamp
);

create table contracts (
    id uuid primary key,
    client_id uuid not null references clients(id),
    contract_number varchar(100) not null unique,
    type varchar(20) not null check (type in ('HEALTH')),
    start_date date not null,
    end_date date not null,
    coverage_limit numeric(12, 2) not null check (coverage_limit > 0),
    reimbursement_rate numeric(5, 4) not null check (reimbursement_rate >= 0 and reimbursement_rate <= 1),
    status varchar(20) not null check (status in ('ACTIVE', 'EXPIRED', 'SUSPENDED'))
);

create table claims (
    id uuid primary key,
    client_id uuid not null references clients(id),
    contract_id uuid not null references contracts(id),
    claim_number varchar(100) not null unique,
    claim_amount numeric(12, 2) not null check (claim_amount > 0),
    reimbursement_amount numeric(12, 2),
    status varchar(20) not null check (status in ('DRAFT', 'SUBMITTED', 'UNDER_REVIEW', 'APPROVED', 'REJECTED', 'PAID')),
    description text,
    medical_service_date date not null,
    submitted_at timestamp,
    reviewed_at timestamp,
    reviewed_by uuid references users(id)
);

create table claim_documents (
    id uuid primary key,
    claim_id uuid not null references claims(id),
    file_name varchar(255) not null,
    file_type varchar(100) not null,
    file_path varchar(500) not null,
    file_size bigint not null,
    uploaded_at timestamp not null default current_timestamp
);

create table claim_comments (
    id uuid primary key,
    claim_id uuid not null references claims(id),
    author_id uuid not null references users(id),
    comment text not null,
    created_at timestamp not null default current_timestamp
);

create index idx_contracts_client_id on contracts(client_id);
create index idx_claims_client_id on claims(client_id);
create index idx_claims_contract_id on claims(contract_id);
create index idx_claim_documents_claim_id on claim_documents(claim_id);
create index idx_claim_comments_claim_id on claim_comments(claim_id);
