package org.example.eurder.feature.item.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.eurder.feature.item.api.dto.ItemDto;
import org.example.eurder.feature.item.api.request.AddItemRequest;
import org.example.eurder.feature.item.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @Secured("ADD_ITEM")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create an item")
    public ItemDto addItem(@RequestBody @Valid AddItemRequest request) {
        return itemService.createItem(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all items")
    public List<ItemDto> listAllItems() {
        return itemService.getAllItems();
    }
}
