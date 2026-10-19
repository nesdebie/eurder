package org.example.eurder.api.controller;

import org.example.eurder.api.dto.UserDto;
import org.example.eurder.api.mapper.UserMapper;
import org.example.eurder.api.request.PostUserRequest;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/customer")
public class UserControler {
    private final UserService service;
    private final UserMapper mapper;
    private final UserRepository repository;

    public UserControler(UserService service, UserMapper mapper, UserRepository repository) {
        this.service = service;
        this.mapper = mapper;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createCustomer(@RequestBody PostUserRequest request) {
        return mapper.mapToDto(service.createCustomer(request));
    }
}
