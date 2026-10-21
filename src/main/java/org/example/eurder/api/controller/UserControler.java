package org.example.eurder.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.example.eurder.api.dto.user.detail.UserDetailsDto;
import org.example.eurder.api.dto.user.UserDto;
import org.example.eurder.api.mapper.UserDetailsMapper;
import org.example.eurder.api.mapper.UserMapper;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.domain.user.Permission;
import org.example.eurder.service.SecurityService;
import org.example.eurder.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/user")
public class UserControler {
    private final UserService service;
    private final UserMapper userMapper;
    private final UserDetailsMapper userDetailsMapper;
    private final SecurityService security;

    public UserControler(
            UserService service,
            UserMapper userMapper,
            UserDetailsMapper userDetailsMapper,
            SecurityService security
    ) {
        this.service = service;
        this.userMapper = userMapper;
        this.userDetailsMapper = userDetailsMapper;
        this.security = security;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createCustomer(
            @RequestBody PostUserRequest request
    ) {
        return userMapper.mapToDto(service.createUser(request));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllCustomers(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email
    ) {
        security.validateAuthorization(email, Permission.VIEW_CUSTOMERS);
        return userMapper.mapToDto(service.getAllCustomers());
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDetailsDto getCustomer(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email,
            @PathVariable UUID id
    ) {
        security.validateAuthorization(email, Permission.VIEW_CUSTOMER);
        return userDetailsMapper.mapToDto(service.getCustomerDetails(id));
    }
}
