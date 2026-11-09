package org.example.eurder.feature.customer.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.eurder.feature.customer.api.dto.CustomerDetailDto;
import org.example.eurder.feature.customer.api.dto.CustomerDto;
import org.example.eurder.feature.customer.api.request.CreateCustomerRequest;
import org.example.eurder.feature.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public CustomerDto createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request);
    }


    @GetMapping()
    @Secured("VIEW_CUSTOMERS")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDto> viewCustomers() {
        return customerService.getAllCustomers();
    }


    @GetMapping(value = "/{id}")
    @Secured("VIEW_CUSTOMER")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDetailDto viewCustomer(@PathVariable UUID id) {
        return customerService.getCustomerDetails(id);
    }
}
