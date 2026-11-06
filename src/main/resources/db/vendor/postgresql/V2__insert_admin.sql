CREATE extension IF NOT EXISTS pgcrypto;

INSERT INTO customers (email, password_hash, role)
VALUES (
        'admin@eurder.com',
        crypt('${adminPassword}', gen_salt('bf')),
        'ADMIN'
       );