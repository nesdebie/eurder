package org.example.eurder.feature.eurder.exception;

import org.example.eurder.feature.item.exception.ItemNotFoundException;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.UUID;

public class EurderForbiddenException extends AuthorizationDeniedException {
    public EurderForbiddenException(String message) {
        super(message);
    }

    public static EurderForbiddenException forbiddenException() {
        throw new EurderForbiddenException("Forbidden.");
    }
}
