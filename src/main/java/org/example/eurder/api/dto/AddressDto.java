package org.example.eurder.api.dto;

public record AddressDto(
        String streetName,
        String streetNumber,
        String city,
        String country
) {}
