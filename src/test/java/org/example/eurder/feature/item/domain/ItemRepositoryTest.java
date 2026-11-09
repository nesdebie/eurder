package org.example.eurder.feature.item.domain;

import org.example.eurder.common.TestData;
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

    @Test
    void givenSavedItem_whenFindingByName_thenReturnsItWithExactPrice() {
        // given
        Item item = itemRepository.save(TestData.item("19.99", 10));
        entityManager.flush();
        entityManager.clear();

        // when
        Item found = itemRepository.findByName(item.getName()).orElseThrow();

        // then
        assertThat(found.getPrice()).isEqualByComparingTo("19.99");
        assertThat(found.getAmount()).isEqualTo(10);
    }

    @Test
    void givenNegativePrice_whenSaving_thenDatabaseRejectsIt() {
        // given
        Item item = new Item(TestData.uniqueName(), "A description", new BigDecimal("-1.00"), 1);

        // when / then
        assertThatThrownBy(() -> itemRepository.saveAndFlush(item))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void givenNameAlreadyStored_whenSavingAnotherItemWithIt_thenDatabaseRejectsIt() {
        // given
        Item first = itemRepository.saveAndFlush(TestData.item("1.00", 1));
        Item duplicate = new Item(first.getName(), "Other", new BigDecimal("2.00"), 2);

        // when / then
        assertThatThrownBy(() -> itemRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
