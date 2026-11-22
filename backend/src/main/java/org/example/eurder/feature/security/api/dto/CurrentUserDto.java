package org.example.eurder.feature.security.api.dto;

import java.util.List;

public record CurrentUserDto(
        String email,
        List<String> permissions
) {
}
