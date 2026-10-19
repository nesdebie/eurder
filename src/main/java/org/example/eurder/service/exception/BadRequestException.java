package org.example.eurder.service.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException customerParameterRequired(String parameter) {
        return new BadRequestException("Valid Customer's %s is required".formatted(parameter));
    }

    public static BadRequestException invalidEmail(String email) {
        return new BadRequestException("Email %s is invalid".formatted(email));
    }

    public static BadRequestException invalidPhoneNumber(String phone) {
        return new BadRequestException("Phone number %s is invalid".formatted(phone));
    }

    public static BadRequestException invalidPrice(double price) {
        return new BadRequestException("Item price %f is invalid".formatted(price));
    }

    public static BadRequestException invalidAmount(int amount) {
        return new BadRequestException("Item amount %d is invalid".formatted(amount));
    }

    public static BadRequestException invalidStreetNumber(String number) {
        return new BadRequestException("Street number %s is invalid".formatted(number));
    }
}
