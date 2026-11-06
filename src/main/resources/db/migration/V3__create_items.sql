CREATE TABLE items (
    id          UUID                DEFAULT gen_random_uuid() PRIMARY KEY,
    name        VARCHAR(50)         NOT NULL UNIQUE,
    description VARCHAR(255)        NOT NULL,
    price       NUMERIC(10,2)       NOT NULL,
    amount      INTEGER             NOT NULL,
    CONSTRAINT chk_items_price_positive  CHECK (price > 0),
    CONSTRAINT chk_items_amount_not_negative CHECK (amount >= 0)
);