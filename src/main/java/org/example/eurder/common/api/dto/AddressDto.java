package org.example.eurder.common.api.dto;

public record AddressDto(
        String streetName,
        String streetNumber,
        String postalCode,
        String city
) {}
