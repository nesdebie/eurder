package org.example.eurder.api.dto.user;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        AddressDto address
) {}
