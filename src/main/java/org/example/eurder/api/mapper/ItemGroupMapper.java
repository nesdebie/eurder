package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.eurder.EurderedItemDto;
import org.example.eurder.api.dto.eurder.ItemGroupDto;
import org.example.eurder.domain.ItemGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemGroupMapper {
        public ItemGroupDto mapToDto(ItemGroup group) {
            return new ItemGroupDto(
                    new EurderedItemDto(group.getItemId(),
                            group.getName(),
                            group.getDescription(),
                            group.getPrice()),
                    group.getAmount(),
                    group.getShippingDate()
            );
        }

        public List<ItemGroupDto> mapToDto(List<ItemGroup> list) {
            return list.stream().map(item -> mapToDto(item)).toList();
        }
}
