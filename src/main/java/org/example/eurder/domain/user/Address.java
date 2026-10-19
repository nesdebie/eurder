package org.example.eurder.domain.user;

public record Address(
        String streetName,
        String number,
        String city,
        String country
) {}
