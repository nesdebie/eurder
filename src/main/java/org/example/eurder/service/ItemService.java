package org.example.eurder.service;

import org.example.eurder.api.request.PostItemRequest;
import org.example.eurder.domain.Item;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository repository;

    public ItemService(ItemRepository repository) {
        this.repository = repository;
    }

    public List<Item> getAllItems() {
        return repository.getAll();
    }

    public Item createItem(PostItemRequest request) {
        verifyParametersAreFilledIn(request);
        if (request.price() < 0)
            throw BadRequestException.invalidPrice(request.price());
        if (request.amount() <= 0)
            throw BadRequestException.invalidAmount(request.amount());

        if (repository.getOptionalByName(request.name()).isPresent())
            throw BadRequestException.alreadyExistingItem(request.name());
        return repository.save(
                new Item(
                        request.name(),
                        request.description(),
                        request.price(),
                        request.amount()
                )
        );
    }

    public Item updateItem(PostItemRequest request) {
        verifyParametersAreFilledIn(request);
        if (request.price() < 0)
            throw BadRequestException.invalidPrice(request.price());
        if (request.amount() < 0)
            throw BadRequestException.invalidAmount(request.amount());
        if (!repository.getOptionalByName(request.name()).isPresent())
            throw BadRequestException.nonExistingItem(request.name());

        Item updatedItem = repository.getOptionalByName(request.name()).get();

        updatedItem.setDescription(request.description());
        updatedItem.setPrice(request.price());
        updatedItem.setAmount(updatedItem.getAmount() + request.amount());

        return repository.save(updatedItem);
    }

    public void verifyParametersAreFilledIn(PostItemRequest request) {
        verifyParameterIsFilledIn(request.name(), "name");
        verifyParameterIsFilledIn(request.description(), "description");
    }

    public void verifyParameterIsFilledIn(String value, String parameterName) {
        if (value == null || value.isEmpty())
            throw BadRequestException.customerParameterRequired(parameterName);
    }
}
