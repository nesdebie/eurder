package org.example.eurder.api.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        AddressDto address
) {}
