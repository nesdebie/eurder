package org.example.eurder.feature.item.exception;

import org.example.eurder.common.exception.NotFoundException;

import java.util.UUID;

public class ItemNotFoundException extends NotFoundException {
    public ItemNotFoundException(String message) {
        super(message);
    }

    public static ItemNotFoundException itemIdNotFound(UUID id) {
        throw new ItemNotFoundException("Item ID %s does not exist.".formatted(id.toString()));
    }
}
