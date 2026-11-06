package org.example.eurder.feature.customer.api;

import org.example.eurder.common.api.AddressMapper;
import org.example.eurder.common.api.FullNameMapper;
import org.example.eurder.feature.customer.api.dto.CreatedCustomerDto;
import org.example.eurder.feature.customer.domain.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final FullNameMapper fullNameMapper;

    public CustomerMapper(AddressMapper addressMapper, FullNameMapper fullNameMapper) {
        this.addressMapper = addressMapper;
        this.fullNameMapper = fullNameMapper;
    }

    public CreatedCustomerDto mapToCreatedDto(Customer customer) {
        return new CreatedCustomerDto(
                fullNameMapper.mapToDto(customer.getFullName()),
                customer.getEmail(),
                customer.getPhoneNumber(),
                addressMapper.mapToDto(customer.getAddress())
        );
    }
}
