package org.example.eurder.feature.item.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemTest {

    private static Item itemWithStock(int stock) {
        return new Item("Fridge", "Cold", new BigDecimal("499.99"), stock);
    }

    @DisplayName("Checking stock if(stock > amount)")
    @Test
    void givenEnoughStock_whenReducingAmount_thenStockDecreases() {
        Item item = itemWithStock(10);

        item.reduceAmount(3);

        assertThat(item.getAmount()).isEqualTo(7);
    }

    @DisplayName("Checking stock if(stock == amount)")
    @Test
    void givenWholeStock_whenReducingAmount_thenStockIsZero() {
        Item item = itemWithStock(3);

        item.reduceAmount(3);

        assertThat(item.getAmount()).isZero();
    }

    @DisplayName("Checking stock if(stock < amount)")
    @Test
    void givenNotEnoughStock_whenReducingAmount_thenStockBecomesNegative() {
        Item item = itemWithStock(2);

        item.reduceAmount(3);

        assertThat(item.getAmount()).isEqualTo(-1);
    }

    @DisplayName("Checking stock if(stock < amount) and stock < 0")
    @Test
    void givenNegativeEnoughStock_whenReducingAmount_thenStockStillDecrease() {
        Item item = itemWithStock(2);

        item.reduceAmount(3);
        item.reduceAmount(1);
        assertThat(item.getAmount()).isEqualTo(-2);
    }

    @DisplayName("Checking less-than-one amount exception")
    @Test
    void givenZeroAmount_whenReducingAmount_thenThrows() {
        Item item = itemWithStock(2);

        assertThatThrownBy(() -> item.reduceAmount(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount to subtract must be greater than 0");
    }

    @DisplayName("Testing all values replaced")
    @Test
    void givenOtherValues_whenUpdating_thenAllFieldsAreReplaced() {
        Item item = itemWithStock(10);

        item.update(new Item("Freezer", "Colder", new BigDecimal("699.00"), 4));

        assertThat(item.getName()).isEqualTo("Freezer");
        assertThat(item.getDescription()).isEqualTo("Colder");
        assertThat(item.getPrice()).isEqualByComparingTo("699.00");
        assertThat(item.getAmount()).isEqualTo(4);
    }

    @DisplayName("Testing null name item creation")
    @Test
    void givenNoName_whenCreatingItem_thenThrows() {
        assertThatThrownBy(() -> new Item(null, "Cold", new BigDecimal("1.00"), 1))
                .isInstanceOf(NullPointerException.class);
    }
}
