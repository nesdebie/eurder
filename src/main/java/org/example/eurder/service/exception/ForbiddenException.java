package org.example.eurder.service.exception;

public class ForbiddenException extends RuntimeException {
    private ForbiddenException(String message) {
        super(message);
    }

    public static ForbiddenException forPermission(String permission) {
        return new ForbiddenException("You don't have permission to " + permission);
    }
}
