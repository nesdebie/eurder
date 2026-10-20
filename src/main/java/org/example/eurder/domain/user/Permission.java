package org.example.eurder.domain.user;

import org.example.eurder.service.exception.ForbiddenException;

public enum Permission {
    ADD_ITEM("Add item"),
    UPDATE_ITEM("Update item"),
    ORDER_ITEM("Order Item"),
    VIEW_REPORT("View an orders report"),
    VIEW_CUSTOMER("View a single customer"),
    VIEW_CUSTOMERS("View all customers"),
    VIEW_SHIPPING("View all items shipped today");

    private final ForbiddenException exception;

    Permission(String description) {
        this.exception = ForbiddenException.forPermission(description);
    }

    public ForbiddenException getException() {
        return exception;
    }
}
