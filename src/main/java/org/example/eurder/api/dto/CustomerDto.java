package org.example.eurder.api.dto;

import org.example.eurder.domain.customer.Address;

import java.util.UUID;

public record CustomerDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        AddressDto address
) {}
