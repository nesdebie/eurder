package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.ItemToShipTodayDto;
import org.example.eurder.api.dto.user.AddressDto;
import org.example.eurder.domain.ItemToShipToday;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemToShipTodayMapper {
    private final UserMapper users;
    private final ItemGroupMapper groups;

    public ItemToShipTodayMapper(UserMapper users, ItemGroupMapper groups) {
        this.users = users;
        this.groups = groups;
    }

    public ItemToShipTodayDto mapToDto(ItemToShipToday today) {
        return new ItemToShipTodayDto(
                groups.mapToDto(today.group()),
                users.mapToDto(today.address())
        );
    }

    public List<ItemToShipTodayDto> mapToDto(List<ItemToShipToday> list) {
        return list.stream().map(item -> mapToDto(item)).toList();
    }
}
