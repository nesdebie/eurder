package org.example.eurder.feature.item.exception;

import org.apache.coyote.BadRequestException;

public class ItemBadRequestException extends BadRequestException {
    public ItemBadRequestException(String message) {
        super(message);
    }

    public static ItemBadRequestException outOfStockException(String filter) {
        throw new ItemNotFoundException("Item %s is out of stock.".formatted(filter));
    }
}
