package org.example.eurder.feature.eurder.domain;

import org.example.eurder.common.TestData;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.customer.domain.CustomerRepository;
import org.example.eurder.feature.item.domain.Item;
import org.example.eurder.feature.item.domain.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EurderRepositoryTest {

    private static final LocalDate ORDER_DATE = LocalDate.of(2026, 11, 9);

    @Autowired
    private EurderRepository eurderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("Testing stored order data")
    @Test
    void givenSavedEurder_whenReadingItBack_thenItemGroupsAndTotalAreStored() {
        Customer customer = customerRepository.save(TestData.customer("hash"));
        Item item = itemRepository.save(TestData.item("19.99", 10));
        Eurder eurder = eurderRepository.save(new Eurder(customer, ORDER_DATE,
                List.of(ItemGroup.of(item, 3, ORDER_DATE))));
        entityManager.flush();
        entityManager.clear();

        Eurder found = eurderRepository.findById(eurder.getId()).orElseThrow();

        assertThat(found.getOrderDate()).isEqualTo(ORDER_DATE);
        assertThat(found.getItemGroups()).hasSize(1);
        ItemGroup itemGroup = found.getItemGroups().get(0);
        assertThat(itemGroup.itemId()).isEqualTo(item.getId());
        assertThat(itemGroup.itemName()).isEqualTo(item.getName());
        assertThat(itemGroup.amount()).isEqualTo(3);
        assertThat(itemGroup.shippingDate()).isEqualTo(LocalDate.of(2026, 11, 10));
        assertThat(found.getTotalPrice()).isEqualByComparingTo("59.97");
    }

    @DisplayName("Testing an customer views his/her own orders")
    @Test
    void givenEurdersOfTwoCustomers_whenFindingByCustomer_thenReturnsOnlyTheirs() {
        Customer john = customerRepository.save(TestData.customer("hash"));
        Customer jane = customerRepository.save(TestData.customer("hash"));
        Item item = itemRepository.save(TestData.item("19.99", 10));
        eurderRepository.save(new Eurder(john, ORDER_DATE, List.of(ItemGroup.of(item, 1, ORDER_DATE))));
        eurderRepository.save(new Eurder(jane, ORDER_DATE, List.of(ItemGroup.of(item, 1, ORDER_DATE))));

        List<Eurder> eurders = eurderRepository.findAllByCustomer(john);

        assertThat(eurders).hasSize(1);
        assertThat(eurders.get(0).getCustomer().getId()).isEqualTo(john.getId());
    }
}
