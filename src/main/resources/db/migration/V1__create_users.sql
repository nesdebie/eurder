create table users
(
    id           uuid         default gen_random_uuid() primary key,
    email        varchar(255) not null unique,
    password_hash varchar(255) not null,
    role         varchar(20)  not null,
    first_name   varchar(255),
    last_name    varchar(255),
    phone_number varchar(50),
    street       varchar(255),
    house_number varchar(20),
    postal_code  varchar(20),
    city         varchar(255),
    constraint chk_users_role
        check (role in ('ADMIN', 'CUSTOMER')),
    constraint chk_customer_has_profile
        check (role <> 'CUSTOMER'
            or (first_name   is not null
                and last_name    is not null
                and phone_number is not null
                and street       is not null
                and house_number is not null
                and postal_code  is not null
                and city         is not null))
);