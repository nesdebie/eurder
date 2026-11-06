CREATE TABLE eurders
(
    id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers (id),
    order_date  DATE NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    CONSTRAINT chk_total_price_positive  CHECK (total_price > 0),
);

CREATE INDEX idx_eurders_customer ON eurders (customer_id);