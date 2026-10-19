package org.example.eurder.api.request;

public record PostCustomerRequest (
        String firstName,
        String lastName,
        String email,
        String address,
        String phoneNumber
) {}
