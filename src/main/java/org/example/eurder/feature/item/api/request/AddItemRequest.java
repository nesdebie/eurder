package org.example.eurder.feature.item.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AddItemRequest(
        @NotBlank(message = "Item name is required")
        @Size(max = 50, message = "Item name must be at most 50 characters")
        @Schema(example = "My super item name")
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must be at most 255 characters")
        @Schema(example = "This item is incredible")
        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits and 2 decimals")
        @Schema(description = "Unit price in euro", example = "19.99")
        BigDecimal price,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be more than 0")
        @Schema(description = "Number of items in stock", example = "10")
        Integer amount
) {}