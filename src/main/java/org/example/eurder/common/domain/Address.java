package org.example.eurder.common.domain;

public record Address(
        String street,
        String houseNumber,
        String postalCode,
        String city
) {}
