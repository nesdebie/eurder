package org.example.eurder.domain.customer;

public record Address(
        String streetName,
        String number,
        String city,
        String country
) {}
