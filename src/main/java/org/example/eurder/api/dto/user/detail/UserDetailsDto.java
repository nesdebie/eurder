package org.example.eurder.api.dto.user.detail;

import org.example.eurder.api.dto.eurder.ItemGroupDto;
import org.example.eurder.api.dto.user.UserDto;

import java.util.List;

public record UserDetailsDto(
        UserDto user,
        List<ItemGroupDto> items
) {
}
