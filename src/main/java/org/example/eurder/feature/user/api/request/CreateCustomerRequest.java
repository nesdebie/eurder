package org.example.eurder.feature.user.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.eurder.common.api.dto.AddressDto;
import org.example.eurder.common.api.dto.FullNameDto;

public record CreateCustomerRequest(
        @NotNull(message = "Full name is required")
        @Valid
        FullNameDto fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        @Schema(description = "Email, also used to log in; spaces and capitals are ignored", example = "jane.doe@gmail.com")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must be between 8 and 72 characters")
        @Schema(description = "Password used to log in, 8 to 72 characters", example = "password123")
        String password,

        @NotBlank(message = "Telephone is required")
        @Size(max = 50, message = "Telephone must be at most 50 characters")
        @Schema(description = "Telephone number", example = "+32 470 12 34 56")
        String phoneNumber,

        @NotNull(message = "Full name is required")
        @Valid
        AddressDto address
) {}
