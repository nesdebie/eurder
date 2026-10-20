package org.example.eurder.domain.user;

import org.example.eurder.service.exception.ForbiddenException;

public enum Permission {
    ADD_ITEM("Add item"),
    UPDATE_ITEM("Update item"),
    ORDER_ITEM("Order Item"),
    VIEW_CUSTOMER("View a single customer"),
    VIEW_CUSTOMERS("View all customers");

    private final ForbiddenException exception;

    Permission(String description) {
        this.exception = ForbiddenException.forPermission(description);
    }

    public ForbiddenException getException() {
        return exception;
    }
}
