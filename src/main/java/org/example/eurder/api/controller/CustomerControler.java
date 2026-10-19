package org.example.eurder.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.eurder.api.dto.CustomerDto;
import org.example.eurder.api.mapper.CustomerMapper;
import org.example.eurder.api.request.PostCustomerRequest;
import org.example.eurder.repository.CustomerRepository;
import org.example.eurder.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/customer")
public class CustomerControler {
    private final CustomerService service;
    private final CustomerMapper mapper;
    private final CustomerRepository repository;

    public CustomerControler(CustomerService service, CustomerMapper mapper, CustomerRepository repository) {
        this.service = service;
        this.mapper = mapper;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto createCustomer(@RequestBody PostCustomerRequest request) {
        return mapper.mapToDto(service.createCustomer(request));
    }
}
