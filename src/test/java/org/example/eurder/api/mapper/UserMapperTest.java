package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.user.UserDto;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {
    private final UserMapper mapper = new UserMapper();
    private User customer;

    @BeforeEach
    void initCustomer() {
        Address address = new Address("Baker Street", "221B", "London", "United Kingdom");
        customer = new User("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", address);
    }

    @DisplayName("Testing mapToDto")
    @Test
    void givenCustomer_whenCallingMapToDto_thenACustomerDtoIsReturned() {
        UserDto dto = mapper.mapToDto(customer);

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