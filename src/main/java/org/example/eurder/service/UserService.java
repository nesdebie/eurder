package org.example.eurder.service;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.validator.routines.EmailValidator;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.ItemGroup;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.Role;
import org.example.eurder.domain.user.User;
import org.example.eurder.repository.EurderRepository;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.example.eurder.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository users;
    private final EurderRepository eurders;
    private final EmailValidator validator;

    public UserService(UserRepository users, EurderRepository eurders) {
        this.users = users;
        this.eurders = eurders;
        this.validator = EmailValidator.getInstance();
    }

    public List<User> getAllCustomers() {
        return users.getAll().stream()
                .filter(user -> user.getRole().equals(Role.CUSTOMER))
                .toList();
    }

    public Pair<User, List<ItemGroup>> getCustomerDetails(UUID id) {
        Optional<User> user = users.get(id);
        if (user == null) throw NotFoundException.userNotFound(id);
        return Pair.of(user.get(), eurders.getItemGroupsByUserId(id));
    }

    public User createUser(PostUserRequest request) {
        verifyParametersAreFilledIn(request);

        if (!validator.isValid(request.email()))
            throw BadRequestException.invalidEmail(request.email());
        if(!request.phoneNumber().chars().allMatch(Character::isDigit))
            throw BadRequestException.invalidPhoneNumber(request.phoneNumber());
        if(!request.streetNumber().matches("^[0-9]+([\\s\\-/]?[a-zA-Z0-9]+)?$"))
            throw BadRequestException.invalidStreetNumber(request.streetNumber());
        if (users.isEmailAlreadyUsed(request.email()))
            throw BadRequestException.invalidEmail(request.email());
        return users.save(
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
