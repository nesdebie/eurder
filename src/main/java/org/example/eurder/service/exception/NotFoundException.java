package org.example.eurder.service.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException userNotFound() {
        return new NotFoundException("User not found");
    }

    public static NotFoundException eurderNotFound(UUID id) {
        return new NotFoundException("Eurder not found");
    }

    public static NotFoundException itemNotFound(UUID id) {
        return new NotFoundException("Item with ID %s not found".formatted(id));
    }
}
