package org.example.eurder.common.api;

import org.example.eurder.common.api.dto.AddressDto;
import org.example.eurder.common.domain.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    public AddressDto mapToDto(Address address) {
        return new AddressDto(
                address.streetName(),
                address.streetNumber(),
                address.postalCode(),
                address.city()
        );
    }

    public Address mapToEmbeddable(AddressDto dto) {
        return new Address(
                dto.streetName(),
                dto.streetNumber(),
                dto.postalCode(),
                dto.city()
        );
    }
}
