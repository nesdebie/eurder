package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.eurder.EurderDto;
import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.Item;
import org.example.eurder.domain.ItemGroup;
import org.example.eurder.domain.user.Address;
import org.example.eurder.domain.user.User;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;


class EurderMapperTest {
    private final ItemGroupMapper itemGroupMapper = new ItemGroupMapper();
    private final UserMapper userMapper = new UserMapper();
    private final EurderMapper eurderMapper = new EurderMapper(itemGroupMapper, userMapper);

    private final UserRepository userRepository = new UserRepository();
    private final ItemRepository itemRepository = new ItemRepository();

    Address address = new Address("Baker Street", "221B", "London", "United Kingdom");
    User customer = new User("Sherlock", "Holmes", "sherlock.holmes@detective.com", "0000000000", address);
    ItemGroup group1 = new ItemGroup(new Item("name", "desc", 10.0, 10), 5);
    ItemGroup group2 = new ItemGroup(new Item("name", "desc", 10.0, 10), 5);
    Eurder eurder = new Eurder(List.of(group1, group2), customer);

    @DisplayName("Testing mapToDto")
    @Test
    void givenEurder_whenCallingMapToDto_thenAnObjectIsReturned() {
        EurderDto dto = eurderMapper.mapToDto(eurder);
        Assertions.assertEquals(dto.id(), eurder.getId());
        Assertions.assertEquals(dto.user().id(), eurder.getUser().getId());
        Assertions.assertEquals(dto.list().size(), eurder.getItems().size());
    }
}