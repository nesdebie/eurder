package org.example.eurder.service.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException customerParameterRequired(String parameter) {
        return new BadRequestException("Valid Customer %s is required".formatted(parameter));
    }
}
