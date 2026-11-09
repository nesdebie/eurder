package org.example.eurder.common.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        String streetName,
        String streetNumber,
        String postalCode,
        String city
) {}
