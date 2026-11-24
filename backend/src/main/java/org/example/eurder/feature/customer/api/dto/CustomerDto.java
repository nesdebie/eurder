package org.example.eurder.feature.customer.api.dto;

import org.example.eurder.common.api.dto.AddressDto;
import org.example.eurder.common.api.dto.FullNameDto;
import java.util.UUID;

public record CustomerDto(
        UUID id,
        FullNameDto fullName,
        String email,
        String phone,
        AddressDto address
) {
}
