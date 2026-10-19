package org.example.eurder.api.request;

public record PostCustomerRequest (
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String street,
        String streetNumber,
        String city,
        String country
) {}
