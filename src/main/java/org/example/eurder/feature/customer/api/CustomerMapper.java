package org.example.eurder.feature.customer.api;

import org.example.eurder.common.api.AddressMapper;
import org.example.eurder.common.api.FullNameMapper;
import org.example.eurder.feature.customer.api.dto.CustomerDto;
import org.example.eurder.feature.customer.domain.Customer;
import org.example.eurder.feature.eurder.api.EurderMapper;
import org.example.eurder.feature.eurder.api.dto.EurdersReportDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {

    private final AddressMapper addressMapper;
    private final FullNameMapper fullNameMapper;

    public CustomerMapper(AddressMapper addressMapper, FullNameMapper fullNameMapper, EurderMapper eurderMapper) {
        this.addressMapper = addressMapper;
        this.fullNameMapper = fullNameMapper;
    }

    public CustomerDto mapToDto(Customer customer) {
        return new CustomerDto(
                fullNameMapper.mapToDto(customer.getFullName()),
                customer.getEmail(),
                customer.getPhoneNumber(),
                addressMapper.mapToDto(customer.getAddress())
        );
    }

    public List<CustomerDto> mapToDto(List<Customer> customers) {
        return customers.stream()
                .map(this::mapToDto)
                .toList();
    }
}
