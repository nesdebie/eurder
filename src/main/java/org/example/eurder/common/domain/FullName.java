package org.example.eurder.common.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record FullName(
        String firstName,
        String lastName
) {
}
