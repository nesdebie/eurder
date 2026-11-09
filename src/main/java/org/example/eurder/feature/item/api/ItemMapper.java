package org.example.eurder.feature.item.api;

import org.example.eurder.feature.item.api.dto.ItemDto;
import org.example.eurder.feature.item.api.request.AddOrUpdateItemRequest;
import org.example.eurder.feature.item.domain.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper {
    public ItemDto mapToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount()
        );
    }

    public List<ItemDto> mapToDto(List<Item> items) {
        return items.stream()
                .map(item -> mapToDto(item))
                .toList();
    }

    public Item mapToEntity(AddOrUpdateItemRequest dto) {
        return new Item(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.amount()
        );
    }


}
