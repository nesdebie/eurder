package org.example.eurder.feature.user.domain;

public record Address(
        String street,
        String houseNumber,
        String postalCode,
        String city
) {}
