package org.example.eurder.feature.eurder.api.dto;

import org.example.eurder.common.api.dto.AddressDto;

import java.util.List;

public record ItemGroupShippedTodayDto(
        AddressDto addressDto,
        List<ItemGroupDto> itemgroups
) {
}
