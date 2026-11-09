package org.example.eurder.feature.item.exception;

import org.apache.coyote.BadRequestException;

public class ItemBadRequestException extends BadRequestException {
    public ItemBadRequestException(String message) {
        super(message);
    }

    public static ItemBadRequestException wrongFilter(String filter) {
        throw new ItemNotFoundException("Filter %s does not exist.".formatted(filter));
    }
}
