package org.example.eurder.service;

import org.example.eurder.api.request.PostCustomerRequest;
import org.example.eurder.domain.Customer;
import org.example.eurder.repository.CustomerRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public Customer createCustomer(PostCustomerRequest request) {
        verifyFilledIn(request.firstName(), "first name");
        verifyFilledIn(request.lastName(), "last name");
        verifyFilledIn(request.email(), "email");
        verifyFilledIn(request.address(), "address");
        verifyFilledIn(request.phoneNumber(), "phone");
        return repository.save(
                new Customer(
                        request.lastName(),
                        request.firstName(),
                        request.email(),
                        request.address(),
                        request.phoneNumber()
                )
        );
    }

    private void verifyFilledIn(String value, String parameterName) {
        if (value == null || value.isEmpty())
            throw BadRequestException.customerParameterRequired(parameterName);
    }
}
