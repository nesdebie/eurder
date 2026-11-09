package org.example.eurder.feature.item.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.eurder.feature.eurder.api.dto.EurdersReportDto;
import org.example.eurder.feature.item.api.dto.ItemDto;
import org.example.eurder.feature.item.api.request.AddOrUpdateItemRequest;
import org.example.eurder.feature.item.service.ItemInventory;
import org.example.eurder.feature.item.service.ItemService;
import org.example.eurder.feature.security.authentication.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public ItemDto addItem(@RequestBody @Valid AddOrUpdateItemRequest request) {
        return itemService.createItem(request);
    }

    @PutMapping(value = "/{id}/update")
    @Secured("UPDATE_ITEM")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update an item")
    public ItemDto addItem(
            @RequestBody @Valid AddOrUpdateItemRequest request,
            @PathVariable UUID id
    ) {
        return itemService.updateItem(request, id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all items")
    public List<ItemDto> listAllItems() {
        return itemService.getAllItems();
    }


    @GetMapping("/inventory")
    @Secured("VIEW_INVENTORY")
    @Operation(summary = "List items by stock resupply urgency, most urgent first")
    public List<ItemDto> viewItemInventory(
            @RequestParam(name = "urgency", required = false) ItemInventory urgency) {
        return itemService.getItemsInventory(urgency);
    }
}
