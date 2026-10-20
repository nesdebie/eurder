package org.example.eurder.api.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.example.eurder.api.dto.AddressDto;
import org.example.eurder.api.dto.UserDetailsDto;
import org.example.eurder.api.dto.UserDto;
import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<UserDto> mapToDto(List<User> list) {
        return list.stream().map(user -> mapToDto(user)).toList();
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
