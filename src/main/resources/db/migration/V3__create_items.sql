CREATE TABLE items (
    id          UUID                DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(50)         NOT NULL UNIQUE,
    description VARCHAR(255)        NOT NULL,
    price       DOUBLE PRECISION    NOT NULL,
    amount      INTEGER             NOT NULL
)