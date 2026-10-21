package org.example.eurder.service;

import org.example.eurder.api.request.PostItemRequest;
import org.example.eurder.api.request.PutItemRequest;
import org.example.eurder.domain.Item;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.example.eurder.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> getAllItems() {
        return repository.getAll();
    }

    public List<Item> getOverviewAllItems(String filter) {
        Predicate<Item> predicate = ItemInventory.fromValue(filter)
                .<Predicate<Item>>map(inv -> item -> inv.matches(item.getAmount()))
                .orElse(item -> true);

        return getAllItems().stream()
                .filter(predicate)
                .sorted(Comparator.comparingInt(Item::getAmount))
                .toList();
    }

    public Item createItem(PostItemRequest request) {
        verifyParametersAreFilledIn(request.name(), request.description());
        verifyNumericParametersAreValid(request.price(), request.amount());

        if (repository.getOptionalByName(request.name()).isPresent())
            throw BadRequestException.itemNameDuplicate(request.name());

        return repository.save(
                new Item(
                        request.name(),
                        request.description(),
                        request.price(),
                        request.amount()
                )
        );
    }

    public Item updateItem(PutItemRequest request) {
        verifyParametersAreFilledIn(request.name(), request.description());
        verifyNumericParametersAreValid(request.price(), request.amount());

        if (!repository.getOptionalById(request.id()).isPresent())
            throw BadRequestException.nonExistingItem(request.name());
        if (repository.getOptionalByName(request.name()).isPresent())
            throw BadRequestException.itemNameDuplicate(request.name());
        Item updatedItem = repository.getOptionalById(request.id())
                .orElseThrow(() -> NotFoundException.itemNotFound(request.id()));

        updatedItem.setName(request.name());
        updatedItem.setDescription(request.description());
        updatedItem.setPrice(request.price());
        updatedItem.setAmount(updatedItem.getAmount() + request.amount());

        return repository.save(updatedItem);
    }

    private void verifyNumericParametersAreValid(double price, int amount) {
        if (price < 0) throw BadRequestException.invalidPrice(price);
        if (amount < 0) throw BadRequestException.invalidAmount(amount);
    }

    private void verifyParametersAreFilledIn(String name, String description) {
        verifyParameterIsFilledIn(name, "name");
        verifyParameterIsFilledIn(description, "description");
    }

    public void verifyParameterIsFilledIn(String value, String parameterName) {
        if (value == null || value.isEmpty()) throw BadRequestException.customerParameterRequired(parameterName);
    }
}
