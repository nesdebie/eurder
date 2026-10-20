package org.example.eurder.api.dto;

import java.util.List;

public record UserDetailsDto(
        UserDto user,
        List<ItemGroupDto> items
) {
}
