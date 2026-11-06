package org.example.eurder.feature.user.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.eurder.feature.user.api.dto.CreatedCustomerDto;
import org.example.eurder.feature.user.api.request.CreateCustomerRequest;
import org.example.eurder.feature.user.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register as a member")
    public CreatedCustomerDto createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }
}
