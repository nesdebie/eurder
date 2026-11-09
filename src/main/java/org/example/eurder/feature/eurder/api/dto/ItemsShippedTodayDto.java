package org.example.eurder.feature.eurder.api.dto;

import java.util.List;

public record ItemsShippedTodayDto(
        List<ItemGroupShippedTodayDto> itemsShippedToday
) {
}
