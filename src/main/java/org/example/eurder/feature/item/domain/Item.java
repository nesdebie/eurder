package org.example.eurder.feature.item.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "items")
public class Item {
    @Id@GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private int amount;

    public Item(String name, String description, BigDecimal price, int amount) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.price = Objects.requireNonNull(price);
        this.amount = amount;
    }

    protected Item() {}

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }
}
