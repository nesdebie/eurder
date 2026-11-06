package org.example.eurder.common.api.dto;

import jakarta.persistence.Embeddable;
import org.example.eurder.feature.item.domain.Item;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
public record ItemGroup(
        UUID itemId,
        String itemName,
        BigDecimal unitPrice,
        int amount,
        LocalDate shippingDate
) {
    public static ItemGroup of(Item item, int amount, LocalDate orderDate) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        LocalDate shippingDate = amount <= item.getAmount()
                ? orderDate.plusDays(1)
                : orderDate.plusWeeks(1);
        return new ItemGroup(item.getId(), item.getName(), item.getPrice(), amount, shippingDate);
    }

    public BigDecimal totalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(amount));
    }
}