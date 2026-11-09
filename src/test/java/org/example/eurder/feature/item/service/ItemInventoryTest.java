package org.example.eurder.feature.item.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class ItemInventoryTest {

    @ParameterizedTest(name = "amount {0} -> {1}")
    @CsvSource({
            "0, STOCK_LOW",
            "4, STOCK_LOW",
            "5, STOCK_MEDIUM",
            "9, STOCK_MEDIUM",
            "10, STOCK_HIGH",
            "1000, STOCK_HIGH"
    })
    void givenAmount_whenGettingUrgency_thenReturnsExpectedLevel(int amount, ItemInventory expected) {
        // when
        ItemInventory urgency = ItemInventory.of(amount);

        // then
        assertThat(urgency).isEqualTo(expected);
    }
}
