package org.example.eurder.feature.eurder.domain;

import org.example.eurder.feature.item.domain.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ItemGroupTest {

    private static final LocalDate ORDER_DATE = LocalDate.of(2026, 11, 9);

    private static Item itemWithStock(int stock) {
        return new Item("Fridge", "Cold", new BigDecimal("499.99"), stock);
    }

    @DisplayName("Checking shipping date with amount < stock")
    @Test
    void givenAmountWithinStock_whenCreatingItemGroup_thenShipsNextDay() {
        Item item = itemWithStock(10);

        ItemGroup itemGroup = ItemGroup.of(item, 3, ORDER_DATE);

        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 10));
    }

    @DisplayName("Checking shipping date with amount == stock")
    @Test
    void givenAmountEqualToStock_whenCreatingItemGroup_thenShipsNextDay() {
        Item item = itemWithStock(3);

        ItemGroup itemGroup = ItemGroup.of(item, 3, ORDER_DATE);

        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 10));
    }

    @DisplayName("Checking shipping date with amount > stock")
    @Test
    void givenAmountAboveStock_whenCreatingItemGroup_thenShipsInSevenDays() {
        Item item = itemWithStock(2);

        ItemGroup itemGroup = ItemGroup.of(item, 3, ORDER_DATE);

        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 16));
    }

    @DisplayName("Checking current item name and price are stored")
    @Test
    void givenItem_whenCreatingItemGroup_thenCopiesNameAndPriceOfThatMoment() {
        Item item = itemWithStock(10);

        ItemGroup itemGroup = ItemGroup.of(item, 2, ORDER_DATE);

        assertThat(itemGroup.itemName()).isEqualTo("Fridge");
        assertThat(itemGroup.unitPrice()).isEqualByComparingTo("499.99");
    }

    @DisplayName("Checking total price")
    @Test
    void givenUnitPriceAndAmount_whenComputingTotalPrice_thenMultipliesThem() {
        ItemGroup itemGroup = ItemGroup.of(itemWithStock(10), 2, ORDER_DATE);

        BigDecimal totalPrice = itemGroup.totalPrice();

        assertThat(totalPrice).isEqualByComparingTo("999.98");
    }

    @DisplayName("Checking zero-amount exception")
    @Test
    void givenZeroAmount_whenCreatingItemGroup_thenThrows() {
        Item item = itemWithStock(10);

        assertThatThrownBy(() -> ItemGroup.of(item, 0, ORDER_DATE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than 0");
    }

    @DisplayName("Checking shipping date with negative stock")
    @Test
    void givenNegativeStock_whenCreatingItemGroup_thenShipsInSevenDays() {
        Item item = itemWithStock(2);
        item.reduceAmount(5);

        ItemGroup itemGroup = ItemGroup.of(item, 1, ORDER_DATE);

        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 16));
    }
}
