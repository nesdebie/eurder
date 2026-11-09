package org.example.eurder.feature.customer.api.dto;

import org.example.eurder.common.api.dto.AddressDto;
import org.example.eurder.common.api.dto.FullNameDto;

public record CustomerDto(
        FullNameDto fullName,
        String email,
        String phone,
        AddressDto address
) {}
