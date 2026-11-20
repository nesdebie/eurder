package org.example.eurder.feature.eurder.exception;

import org.springframework.security.authorization.AuthorizationDeniedException;

public class EurderForbiddenException extends AuthorizationDeniedException {
    public EurderForbiddenException(String message) {
        super(message);
    }

    public static EurderForbiddenException forbiddenException() {
        throw new EurderForbiddenException("Forbidden.");
    }
}
