package org.example.eurder.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.User;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final EmailValidator validator;

    public UserService(UserRepository repository) {
        this.repository = repository;
        this.validator = EmailValidator.getInstance();
    }


    public User createUser(PostUserRequest request) {
        verifyParametersAreFilledIn(request);

        if (!validator.isValid(request.email()))
            throw BadRequestException.invalidEmail(request.email());
        if(!request.phoneNumber().chars().allMatch(Character::isDigit))
            throw BadRequestException.invalidPhoneNumber(request.phoneNumber());
        if(!request.streetNumber().matches("^[0-9]+([\\s\\-/]?[a-zA-Z0-9]+)?$"))
            throw BadRequestException.invalidStreetNumber(request.streetNumber());
        if (!repository.isEmailAvailable(request.email()))
            throw BadRequestException.invalidEmail(request.email());
        return repository.save(
                new User(
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        request.phoneNumber(),
                        new Address(
                                request.street(),
                                request.streetNumber(),
                                request.city(),
                                request.country()
                        )
                )
        );
    }


    private void verifyParametersAreFilledIn(PostUserRequest request) {
        verifyParameterIsFilledIn(request.firstName(), "first name");
        verifyParameterIsFilledIn(request.lastName(), "last name");
        verifyParameterIsFilledIn(request.email(), "email");
        verifyParameterIsFilledIn(request.phoneNumber(), "phone");
        verifyParameterIsFilledIn(request.street(), "street");
        verifyParameterIsFilledIn(request.streetNumber(), "street number");
        verifyParameterIsFilledIn(request.city(), "city");
        verifyParameterIsFilledIn(request.country(), "country");
    }

    public void verifyParameterIsFilledIn(String value, String parameterName) {
        if (value == null || value.isEmpty())
            throw BadRequestException.customerParameterRequired(parameterName);
    }
}
