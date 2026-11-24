package org.example.eurder.feature.eurder.domain;

import org.example.eurder.common.TestData;
import org.example.eurder.feature.customer.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EurderTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 9);
    private static final Customer CUSTOMER = TestData.customer("hash");

    private static ItemGroup itemGroup(String unitPrice, int amount, LocalDate shippingDate) {
        return new ItemGroup(UUID.randomUUID(), "Item", new BigDecimal(unitPrice), amount, shippingDate);
    }

    @DisplayName("Checking order's total price")
    @Test
    void givenTwoItemGroups_whenCreatingEurder_thenTotalPriceIsSumOfItemGroupTotals() {
        List<ItemGroup> itemGroups = List.of(itemGroup("19.99", 3, TODAY), itemGroup("0.10", 2, TODAY));

        Eurder eurder = new Eurder(CUSTOMER, TODAY, itemGroups);

        assertThat(eurder.getTotalPrice()).isEqualByComparingTo("60.17");
    }

    @DisplayName("Testing no-item-group exception")
    @Test
    void givenNoItemGroups_whenCreatingEurder_thenThrows() {
        assertThatThrownBy(() -> new Eurder(CUSTOMER, TODAY, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("An eurder must contain at least one item group");
    }

    @DisplayName("Testing null-item-group exception")
    @Test
    void givenNullItemGroups_whenCreatingEurder_thenThrows() {
        assertThatThrownBy(() -> new Eurder(CUSTOMER, TODAY, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Testing different shipping dates item groups")
    @Test
    void givenItemGroupsWithDifferentShippingDates_whenGettingItemGroupsShippingOnDate_thenReturnsOnlyThoseOfThatDate() {
        ItemGroup tomorrow = itemGroup("1.00", 1, TODAY.plusDays(1));
        ItemGroup nextWeek = itemGroup("1.00", 1, TODAY.plusDays(7));
        Eurder eurder = new Eurder(CUSTOMER, TODAY, List.of(tomorrow, nextWeek));

        List<ItemGroup> shipping = eurder.getItemGroupsShippingOn(TODAY.plusDays(1));

        assertThat(shipping).containsExactly(tomorrow);
    }

    @DisplayName("Testing Eurder data persistence")
    @Test
    void givenEurder_whenModifyingReturnedItemGroups_thenEurderIsUnchanged() {
        Eurder eurder = new Eurder(CUSTOMER, TODAY, List.of(itemGroup("1.00", 1, TODAY)));

        assertThatThrownBy(() -> eurder.getItemGroups().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(eurder.getItemGroups()).hasSize(1);
    }
}
