CREATE TABLE users
(
    id            UUID         DEFAULT gen_rANDom_uuid() PRIMARY KEY ,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(20)  NOT NULL,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    phone_number  VARCHAR(50),
    street_name   VARCHAR(255),
    house_number  VARCHAR(20),
    postal_code   VARCHAR(20),
    city          VARCHAR(255),
    CONSTRAINT chk_users_role
        CHECK (role in ('ADMIN', 'CUSTOMER')),
    CONSTRAINT chk_customer_has_profile
        CHECK (role <> 'CUSTOMER'
            OR (first_name       IS NOT NULL
                AND last_name    IS NOT NULL
                AND phone_number IS NOT NULL
                AND street_name  IS NOT NULL
                AND house_number IS NOT NULL
                AND postal_code  IS NOT NULL
                AND city         IS NOT NULL))
);