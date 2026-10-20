package org.example.eurder.service.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException userNotFound(UUID id) {
        return new NotFoundException("The user with the id %s could not be found".formatted(id));
    }

    public static NotFoundException eurderNotFound(UUID id) {
        return new NotFoundException("The eurder with the id %s could not be found".formatted(id));
    }
}
