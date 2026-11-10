package org.example.eurder.feature.item.domain;

import org.example.eurder.common.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Checking item stored data")
    @Test
    void givenSavedItem_whenFindingByName_thenReturnsItWithExactPrice() {
        Item item = itemRepository.save(TestData.item("19.99", 10));
        entityManager.flush();
        entityManager.clear();

        Item found = itemRepository.findByName(item.getName()).orElseThrow();

        assertThat(found.getPrice()).isEqualByComparingTo("19.99");
        assertThat(found.getAmount()).isEqualTo(10);
    }

    @DisplayName("Testing negative price")
    @Test
    void givenNegativePrice_whenSaving_thenDatabaseRejectsIt() {
        Item item = new Item(TestData.uniqueName(), "A description", new BigDecimal("-1.00"), 1);

        assertThatThrownBy(() -> itemRepository.saveAndFlush(item))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Testing add a duplicate name")
    @Test
    void givenNameAlreadyStored_whenSavingAnotherItemWithIt_thenDatabaseRejectsIt() {
        Item first = itemRepository.saveAndFlush(TestData.item("1.00", 1));
        Item duplicate = new Item(first.getName(), "Other", new BigDecimal("2.00"), 2);

        assertThatThrownBy(() -> itemRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
