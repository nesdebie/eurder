package org.example.eurder.api.dto.user;

public record AddressDto(
        String streetName,
        String streetNumber,
        String city,
        String country
) {}
