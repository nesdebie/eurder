package org.example.eurder.common.api;

import org.example.eurder.common.api.dto.AddressDto;
import org.example.eurder.common.domain.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressDto mapToDto(Address address) {
        return new AddressDto(
                address.street(),
                address.houseNumber(),
                address.postalCode(),
                address.city()
        );
    }
}
