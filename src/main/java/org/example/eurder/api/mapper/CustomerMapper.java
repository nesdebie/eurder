package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.AddressDto;
import org.example.eurder.api.dto.CustomerDto;
import org.example.eurder.domain.customer.Address;
import org.example.eurder.domain.customer.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto mapToDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                mapToDto(customer.getAddress())
        );
    }

    public AddressDto mapToDto(Address address) {
        return new AddressDto(
                address.streetName(),
                address.number(),
                address.city(),
                address.country()
        );
    }
}
