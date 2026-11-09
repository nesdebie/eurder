package org.example.eurder.feature.eurder.domain;

import org.example.eurder.common.TestData;
import org.example.eurder.feature.customer.domain.Customer;
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

    @Test
    void givenTwoItemGroups_whenCreatingEurder_thenTotalPriceIsSumOfItemGroupTotals() {
        // given
        List<ItemGroup> itemGroups = List.of(itemGroup("19.99", 3, TODAY), itemGroup("0.10", 2, TODAY));

        // when
        Eurder eurder = new Eurder(CUSTOMER, TODAY, itemGroups);

        // then
        assertThat(eurder.getTotalPrice()).isEqualByComparingTo("60.17");
    }

    @Test
    void givenNoItemGroups_whenCreatingEurder_thenThrows() {
        // when / then
        assertThatThrownBy(() -> new Eurder(CUSTOMER, TODAY, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("An eurder must contain at least one item group");
    }

    @Test
    void givenNullItemGroups_whenCreatingEurder_thenThrows() {
        // when / then
        assertThatThrownBy(() -> new Eurder(CUSTOMER, TODAY, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenItemGroupsWithDifferentShippingDates_whenGettingItemGroupsShippingOnDate_thenReturnsOnlyThoseOfThatDate() {
        // given
        ItemGroup tomorrow = itemGroup("1.00", 1, TODAY.plusDays(1));
        ItemGroup nextWeek = itemGroup("1.00", 1, TODAY.plusDays(7));
        Eurder eurder = new Eurder(CUSTOMER, TODAY, List.of(tomorrow, nextWeek));

        // when
        List<ItemGroup> shipping = eurder.getItemGroupsShippingOn(TODAY.plusDays(1));

        // then
        assertThat(shipping).containsExactly(tomorrow);
    }

    @Test
    void givenEurder_whenModifyingReturnedItemGroups_thenEurderIsUnchanged() {
        // given
        Eurder eurder = new Eurder(CUSTOMER, TODAY, List.of(itemGroup("1.00", 1, TODAY)));

        // when / then
        assertThatThrownBy(() -> eurder.getItemGroups().clear())
                .isInstanceOf(UnsupportedOperationException.class);
        assertThat(eurder.getItemGroups()).hasSize(1);
    }
}
