package org.example.eurder.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.eurder.api.dto.ItemDto;
import org.example.eurder.api.mapper.ItemMapper;
import org.example.eurder.api.request.PostItemRequest;
import org.example.eurder.api.request.PutItemRequest;
import org.example.eurder.domain.user.Permission;
import org.example.eurder.service.ItemService;
import org.example.eurder.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/item")
public class ItemController {
    private ItemMapper mapper;
    private ItemService service;
    private SecurityService security;

    public ItemController(ItemMapper mapper, ItemService service, SecurityService security) {
        this.mapper = mapper;
        this.service = service;
        this.security = security;
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto addItem(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email,
            @RequestBody PostItemRequest request
    ) {
        security.validateAuthorization(email, Permission.ADD_ITEM);
        return mapper.mapToDto(service.createItem(request));
    }

    @SecurityRequirement(name = "Authorization")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email,
            @RequestBody PutItemRequest request
    ) {
        security.validateAuthorization(email, Permission.UPDATE_ITEM);
        return mapper.mapToDto(service.updateItem(request));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAllItems() {
        return mapper.mapToDto(service.getAllItems());
    }
}
