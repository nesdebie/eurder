package org.example.eurder.feature.eurder.domain;

import org.example.eurder.feature.item.domain.Item;
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

    @Test
    void givenAmountWithinStock_whenCreatingItemGroup_thenShipsNextDay() {
        // given
        Item item = itemWithStock(10);

        // when
        ItemGroup itemGroup = ItemGroup.of(item, 3, ORDER_DATE);

        // then
        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 10));
    }

    @Test
    void givenAmountEqualToStock_whenCreatingItemGroup_thenShipsNextDay() {
        // given
        Item item = itemWithStock(3);

        // when
        ItemGroup itemGroup = ItemGroup.of(item, 3, ORDER_DATE);

        // then
        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 10));
    }

    @Test
    void givenAmountAboveStock_whenCreatingItemGroup_thenShipsInSevenDays() {
        // given
        Item item = itemWithStock(2);

        // when
        ItemGroup itemGroup = ItemGroup.of(item, 3, ORDER_DATE);

        // then
        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 16));
    }

    @Test
    void givenItem_whenCreatingItemGroup_thenCopiesNameAndPriceOfThatMoment() {
        // given
        Item item = itemWithStock(10);

        // when
        ItemGroup itemGroup = ItemGroup.of(item, 2, ORDER_DATE);

        // then
        assertThat(itemGroup.itemName()).isEqualTo("Fridge");
        assertThat(itemGroup.unitPrice()).isEqualByComparingTo("499.99");
    }

    @Test
    void givenUnitPriceAndAmount_whenComputingTotalPrice_thenMultipliesThem() {
        // given
        ItemGroup itemGroup = ItemGroup.of(itemWithStock(10), 2, ORDER_DATE);

        // when
        BigDecimal totalPrice = itemGroup.totalPrice();

        // then
        assertThat(totalPrice).isEqualByComparingTo("999.98");
    }

    @Test
    void givenZeroAmount_whenCreatingItemGroup_thenThrows() {
        // given
        Item item = itemWithStock(10);

        // when / then
        assertThatThrownBy(() -> ItemGroup.of(item, 0, ORDER_DATE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount must be greater than 0");
    }
}
