package org.example.eurder.feature.security.exception;

import org.example.eurder.common.exception.ConflictException;

public class EmailAlreadyUsedException extends ConflictException {

    public EmailAlreadyUsedException(String email) {
        super(messageFor(email));
    }

    private static String messageFor(String email) {
        return "Email " + email + " is already in use";
    }
}
