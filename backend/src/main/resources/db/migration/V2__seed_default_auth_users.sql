insert into users (id, email, password_hash, role, enabled, created_at)
values
    ('11111111-1111-1111-1111-111111111111', 'admin@insurance.local', '$2a$10$3.Ofw.rQvYw//3Y.Aw/gfOR0GLEoS62rPi2zdRYFmVB3MThXaCaW.', 'ADMIN', true, current_timestamp),
    ('22222222-2222-2222-2222-222222222222', 'agent@insurance.local', '$2a$10$3.Ofw.rQvYw//3Y.Aw/gfOR0GLEoS62rPi2zdRYFmVB3MThXaCaW.', 'AGENT', true, current_timestamp),
    ('33333333-3333-3333-3333-333333333333', 'client@insurance.local', '$2a$10$3.Ofw.rQvYw//3Y.Aw/gfOR0GLEoS62rPi2zdRYFmVB3MThXaCaW.', 'CLIENT', true, current_timestamp);

insert into clients (id, user_id, first_name, last_name, phone, address, national_id, date_of_birth, registration_date)
values ('44444444-4444-4444-4444-444444444444', '33333333-3333-3333-3333-333333333333', 'Default', 'Client', '+21612345678', 'Tunis', 'CL-0001', date '1992-04-18', current_timestamp);
