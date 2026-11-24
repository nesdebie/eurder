package org.example.eurder.feature.eurder.exception;

import org.example.eurder.common.exception.NotFoundException;

import java.util.UUID;

public class EurderNotFoundException extends NotFoundException {
    public EurderNotFoundException(String message) {
        super(message);
    }

    public static EurderNotFoundException eurderIdNotFound(UUID id) {
        throw new EurderNotFoundException("Eurder ID %s does not exist.".formatted(id.toString()));
    }
}
