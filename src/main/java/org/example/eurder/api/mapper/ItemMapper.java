package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.ItemDto;
import org.example.eurder.domain.Item;
import org.springframework.stereotype.Component;

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
}
