package org.example.eurder.feature.user.api.dto;

import org.example.eurder.common.api.dto.AddressDto;
import org.example.eurder.common.api.dto.FullNameDto;

public record CreatedCustomerDto(
        FullNameDto fullName,
        String email,
        String phone,
        AddressDto address
) {}
