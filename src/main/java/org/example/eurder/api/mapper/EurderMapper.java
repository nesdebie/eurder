package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.EurderDto;
import org.example.eurder.domain.Eurder;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public List<EurderDto> mapToDto(List<Eurder> eurders) {
        return eurders.stream().map(eurder -> mapToDto(eurder)).toList();
    }
}
