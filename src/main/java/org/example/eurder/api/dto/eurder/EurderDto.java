package org.example.eurder.api.dto.eurder;

import org.example.eurder.api.dto.user.UserDto;

import java.util.List;
import java.util.UUID;

public record EurderDto(
        UUID id,
        List<ItemGroupDto> list,
        UserDto user
) {}