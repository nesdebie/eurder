package org.example.eurder.feature.item.service;

import org.example.eurder.feature.item.api.ItemMapper;
import org.example.eurder.feature.item.api.dto.ItemDto;
import org.example.eurder.feature.item.api.request.AddItemRequest;
import org.example.eurder.feature.item.domain.Item;
import org.example.eurder.feature.item.domain.ItemRepository;
import org.example.eurder.feature.item.exception.ItemConflictException;
import org.example.eurder.feature.security.exception.EmailAlreadyUsedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(AddItemRequest request) {
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


    public List<ItemDto> getAllItems() {
        return itemMapper.mapToDto(itemRepository.findAll());
    }


    private void verifyItemNameHasNoDuplicate(String name) {
        itemRepository.findByName(name)
                .ifPresent(item -> new ItemConflictException(name));
    }
}
