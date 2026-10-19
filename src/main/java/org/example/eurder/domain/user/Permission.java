package org.example.eurder.domain.user;

import org.example.eurder.service.exception.ForbiddenException;

public enum Permission {
    ADD_ITEM("Add item"),
    ORDER_ITEM("Order Item");

    private final ForbiddenException exception;

    Permission(String description) {
        this.exception = ForbiddenException.forPermission(description);
    }

    public ForbiddenException getException() {
        return exception;
    }
}
