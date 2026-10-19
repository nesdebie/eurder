package org.example.eurder.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.example.eurder.api.request.PostCustomerRequest;
import org.example.eurder.domain.customer.Address;
import org.example.eurder.domain.customer.Customer;
import org.example.eurder.repository.CustomerRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    private final EmailValidator validator;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
        this.validator = EmailValidator.getInstance();
    }

    public Customer createCustomer(PostCustomerRequest request) {
        verifyParametersAreFilledIn(request);

        if (!validator.isValid(request.email()))
            throw BadRequestException.invalidEmail(request.email());
        if(!request.phoneNumber().chars().allMatch(Character::isDigit))
            throw BadRequestException.invalidPhoneNumber(request.phoneNumber());
        if(!request.streetNumber().matches("^[0-9]+([\\s\\-/]?[a-zA-Z0-9]+)?$"))
            throw BadRequestException.invalidStreetNumber(request.streetNumber());

        return repository.save(
                new Customer(
                        request.lastName(),
                        request.firstName(),
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

    private void verifyParametersAreFilledIn(PostCustomerRequest request) {
        verifyParameterIsFilledIn(request.firstName(), "first name");
        verifyParameterIsFilledIn(request.lastName(), "last name");
        verifyParameterIsFilledIn(request.email(), "email");
        verifyParameterIsFilledIn(request.phoneNumber(), "phone");
        verifyParameterIsFilledIn(request.street(), "street");
        verifyParameterIsFilledIn(request.streetNumber(), "street number");
    }

    private void verifyParameterIsFilledIn(String value, String parameterName) {
        if (value == null || value.isEmpty())
            throw BadRequestException.customerParameterRequired(parameterName);
    }
}
