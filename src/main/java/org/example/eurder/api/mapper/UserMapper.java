package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.AddressDto;
import org.example.eurder.api.dto.UserDto;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToDto(User customer) {
        return new UserDto(
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
