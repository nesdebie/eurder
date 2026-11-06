package org.example.eurder.feature.item.service;

import org.example.eurder.feature.item.api.ItemMapper;
import org.example.eurder.feature.item.api.dto.ItemDto;
import org.example.eurder.feature.item.api.request.AddOrUpdateItemRequest;
import org.example.eurder.feature.item.domain.Item;
import org.example.eurder.feature.item.domain.ItemRepository;
import org.example.eurder.feature.item.exception.ItemConflictException;
import org.example.eurder.feature.item.exception.ItemNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(AddOrUpdateItemRequest request) {
        verifyItemNameHasNoDuplicate(request.name());

        Item item = new Item(
                request.name(),
                request.description(),
                request.price(),
                request.amount()
        );

        try {
            return itemMapper.mapToDto(itemRepository.saveAndFlush(item));
        } catch (DataIntegrityViolationException e) {
            throw new ItemConflictException(request.name());
        }
    }

    public ItemDto updateItem(AddOrUpdateItemRequest request, UUID id) {
        verifyItemNameHasNoDuplicate(request.name());
        Item item = getItemById(id);

        item.update(itemMapper.mapToEntity(request));

        try {
            return itemMapper.mapToDto(itemRepository.saveAndFlush(item));
        } catch (DataIntegrityViolationException e) {
            throw new ItemConflictException(request.name());
        }
    }


    public List<ItemDto> getAllItems() {
        return itemMapper.mapToDto(
                itemRepository.findAll().stream()
                        .filter(item -> item.getAmount() > 0)
                        .toList()
        );
    }


    private void verifyItemNameHasNoDuplicate(String name) {
        itemRepository.findByName(name)
                .ifPresent(item -> new ItemConflictException(name));
    }

    public Item getItemById(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> ItemNotFoundException.itemIdNotFound(id));
    }
}
