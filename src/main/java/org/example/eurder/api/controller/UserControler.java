package org.example.eurder.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.example.eurder.api.dto.UserDto;
import org.example.eurder.api.mapper.UserMapper;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.user.Permission;
import org.example.eurder.service.SecurityService;
import org.example.eurder.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserControler {
    private final UserService service;
    private final UserMapper mapper;
    private final SecurityService security;

    public UserControler(UserService service, UserMapper mapper, SecurityService security) {
        this.service = service;
        this.mapper = mapper;
        this.security = security;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createCustomer(@RequestBody PostUserRequest request) {
        return mapper.mapToDto(service.createUser(request));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllCustomers(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email
    ) {
        security.validateAuthorization(email, Permission.VIEW_CUSTOMERS);
        return mapper.mapToDto(service.getAllCustomers());
    }
}
