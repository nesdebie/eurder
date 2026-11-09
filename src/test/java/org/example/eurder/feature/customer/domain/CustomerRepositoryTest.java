package org.example.eurder.feature.customer.domain;

import org.example.eurder.common.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void givenSavedCustomer_whenReadingItBack_thenFullNameAndAddressAreStored() {
        Customer customer = customerRepository.save(TestData.customer("hash"));
        entityManager.flush();
        entityManager.clear();

        Customer found = customerRepository.findByEmail(customer.getEmail()).orElseThrow();

        assertThat(found.getFullName().firstName()).isEqualTo("John");
        assertThat(found.getAddress()).isEqualTo(TestData.address());
        assertThat(found.getPhoneNumber()).isEqualTo("0470123456");
    }

    @Test
    void givenSavedAdmin_whenReadingItBack_thenProfileIsEmpty() {
        Customer admin = customerRepository.save(TestData.admin("hash"));
        entityManager.flush();
        entityManager.clear();

        Customer found = customerRepository.findById(admin.getId()).orElseThrow();

        assertThat(found.getFullName()).isNull();
        assertThat(found.getAddress()).isNull();
    }

    @Test
    void givenEmailAlreadyStored_whenSavingAnotherCustomerWithIt_thenDatabaseRejectsIt() {
        Customer first = customerRepository.saveAndFlush(TestData.customer("hash"));
        Customer duplicate = Customer.customer(first.getEmail(), "hash",
                first.getFullName(), "0470000000", TestData.address());

        assertThatThrownBy(() -> customerRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
