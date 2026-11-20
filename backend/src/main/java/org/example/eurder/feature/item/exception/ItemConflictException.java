package org.example.eurder.feature.item.exception;

import org.example.eurder.common.exception.ConflictException;

public class ItemConflictException extends ConflictException {
    public ItemConflictException(String message) {
        super(forMessage(message));
    }

    private static String forMessage(String name) {
        return "Item name %s already exists.".formatted(name);
    }
}
