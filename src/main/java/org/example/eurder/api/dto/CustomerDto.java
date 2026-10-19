package org.example.eurder.api.dto;

import java.util.UUID;

public record CustomerDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String address,
        String phoneNumber
) {}
