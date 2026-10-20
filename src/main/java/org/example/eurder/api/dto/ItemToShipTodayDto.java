package org.example.eurder.api.dto;

import org.example.eurder.api.dto.eurder.ItemGroupDto;
import org.example.eurder.api.dto.user.AddressDto;

public record ItemToShipTodayDto(
        ItemGroupDto group,
        AddressDto address
) {
}
