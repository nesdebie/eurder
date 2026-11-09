package org.example.eurder.common.api;

import org.example.eurder.common.api.dto.FullNameDto;
import org.example.eurder.common.domain.FullName;
import org.springframework.stereotype.Component;

@Component
public class FullNameMapper {
    public FullNameDto mapToDto(FullName fullName) {
        return new FullNameDto(
                fullName.firstName(),
                fullName.lastName()
        );
    }

    public FullName mapToEmbeddable(FullNameDto dto) {
        return new FullName(
                dto.firstName(),
                dto.lastName()
        );
    }
}
