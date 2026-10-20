package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.EurderDto;
import org.example.eurder.domain.Eurder;
import org.springframework.stereotype.Component;

@Component
public class EurderMapper {
    private ItemGroupMapper groupMapper;
    private UserMapper userMapper;

    public EurderMapper(ItemGroupMapper groupMapper, UserMapper userMapper) {
        this.groupMapper = groupMapper;
        this.userMapper = userMapper;
    }

    public EurderDto mapToDto(Eurder eurder) {
        return new EurderDto(
                eurder.getId(),
                groupMapper.mapToDto(eurder.getItems()),
                userMapper.mapToDto(eurder.getUser()
                )
        );
    }
}
