CREATE TABLE item_groups
(
    eurder_id     UUID           NOT NULL REFERENCES eurders (id),
    item_id       UUID           NOT NULL REFERENCES items (id),
    item_name     VARCHAR(50)    NOT NULL,
    unit_price    NUMERIC(10, 2) NOT NULL,
    amount        INTEGER        NOT NULL,
    shipping_date DATE           NOT NULL,
    CONSTRAINT chk_item_groups_amount_positive     CHECK (amount > 0),
    CONSTRAINT chk_item_groups_unit_price_positive CHECK (unit_price > 0)
);

CREATE INDEX idx_item_groups_eurder ON item_groups (eurder_id);
CREATE INDEX idx_item_groups_shipping_date ON item_groups (shipping_date);