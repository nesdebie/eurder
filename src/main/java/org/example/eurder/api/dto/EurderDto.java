package org.example.eurder.api.dto;

import java.util.List;
import java.util.UUID;

public record EurderDto(
        UUID id,
        List<ItemGroupDto> list,
        UserDto user
) {}