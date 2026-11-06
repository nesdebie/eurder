create extension if not exists pgcrypto;

insert into users (email, password_hash, role)
values ('admin@eurder.com', crypt('${adminPassword}', gen_salt('bf')), 'ADMIN');