package org.example.eurder.common.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank(message = "Street name is required")
        @Size(max = 255, message = "Street name must be at most 255 characters")
        @Schema(example = "Rue du Lombard")
        String streetName,

        @NotBlank(message = "Street number is required")
        @Size(max = 20, message = "Street number must be at most 20 characters")
        @Schema(example = "10")
        String streetNumber,

        @NotBlank(message = "Postal code is required")
        @Size(max = 20, message = "Postal code must be at most 20 characters")
        @Schema(example = "1000")
        String postalCode,

        @NotBlank(message = "City is required")
        @Size(max = 255, message = "City must be at most 255 characters")
        @Schema(example = "Brussels")
        String city
) {}