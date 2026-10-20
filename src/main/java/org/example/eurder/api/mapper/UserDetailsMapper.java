package org.example.eurder.api.mapper;

import org.apache.commons.lang3.tuple.Pair;
import org.example.eurder.api.dto.UserDetailsDto;
import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.ItemGroup;
import org.example.eurder.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class UserDetailsMapper {

    private final ItemGroupMapper itemGroupMapper;
    private final UserMapper userMapper;

    public UserDetailsMapper(ItemGroupMapper itemGroupMapper, UserMapper userMapper) {
        this.itemGroupMapper = itemGroupMapper;
        this.userMapper = userMapper;
    }

    public UserDetailsDto mapToDto(Pair<User, List<ItemGroup>> pair) {
        return new UserDetailsDto(
                userMapper.mapToDto(pair.getLeft()),
                itemGroupMapper.mapToDto(pair.getRight())
        );
    }
}
