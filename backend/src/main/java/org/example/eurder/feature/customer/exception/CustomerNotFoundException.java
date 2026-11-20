package org.example.eurder.feature.customer.exception;

import org.example.eurder.common.exception.NotFoundException;

import java.util.UUID;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException(String message) {
        super(message);
    }

    public static CustomerNotFoundException customerIdNotFound(UUID id) {
        throw new CustomerNotFoundException("Customer ID %s does not exist.".formatted(id.toString()));
    }
}
