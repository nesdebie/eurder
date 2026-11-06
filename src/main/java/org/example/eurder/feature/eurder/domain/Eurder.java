package org.example.eurder.feature.eurder.domain;

import jakarta.persistence.*;
import org.example.eurder.feature.customer.domain.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "eurders")
public class Eurder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private LocalDate orderDate;

    @ElementCollection
    @CollectionTable(name = "item_groups", joinColumns = @JoinColumn(name = "eurder_id"))
    private List<ItemGroup> itemGroups;

    private BigDecimal totalPrice;

    public Eurder(Customer customer, LocalDate orderDate, List<ItemGroup> itemGroups) {
        if (itemGroups == null || itemGroups.isEmpty()) {
            throw new IllegalArgumentException("An eurder must contain at least one item group");
        }
        this.customer = Objects.requireNonNull(customer);
        this.orderDate = Objects.requireNonNull(orderDate);
        this.itemGroups = new ArrayList<>(itemGroups);
        this.totalPrice = getTotalPrice();
    }

    protected Eurder() {}

    private BigDecimal getTotalPrice() {
        return getItemGroups().stream()
                .map(itemGroup -> itemGroup.totalPrice())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ItemGroup> getItemGroups() {
        return List.copyOf(itemGroups);
    }
}