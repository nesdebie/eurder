package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.CustomerDto;
import org.example.eurder.domain.customer.Address;
import org.example.eurder.domain.customer.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {
    private final CustomerMapper mapper = new CustomerMapper();
    private Customer customer;

    @BeforeEach
    void initCustomer() {
        Address address = new Address("Baker Street", "221B", "London", "United Kingdom");
        customer = new Customer("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", address);
    }

    @DisplayName("Testing mapToDto")
    @Test
    void givenCustomer_whenCallingMapToDto_thenACustomerDtoIsReturned() {
        CustomerDto dto = mapper.mapToDto(customer);

        assertEquals(customer.getFirstName(), dto.firstName());
        assertEquals(customer.getLastName(), dto.lastName());
        assertEquals(customer.getEmail(), dto.email());
        assertEquals(customer.getPhoneNumber(), dto.phoneNumber());
        assertEquals(customer.getAddress().streetName(), dto.address().streetName());
        assertEquals(customer.getAddress().streetName(), dto.address().streetName());
        assertEquals(customer.getAddress().streetName(), dto.address().streetName());
        assertEquals(customer.getAddress().streetName(), dto.address().streetName());
    }
}