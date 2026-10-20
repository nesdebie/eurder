package org.example.eurder.api.dto.eurder;

import java.time.LocalDate;

public record ItemGroupDto(
        EurderedItemDto item,
        int amount,
        LocalDate shippingDate
) {}
