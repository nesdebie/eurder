package org.example.eurder.service;

import org.example.eurder.api.request.PostEurderRequest;
import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.Item;
import org.example.eurder.domain.ItemGroup;
import org.example.eurder.repository.EurderRepository;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EurderService {
    private EurderRepository eurderRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    public EurderService(EurderRepository eurderRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.eurderRepository = eurderRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Eurder createEurder(PostEurderRequest request, String user) {
        checkOrdersValidity(request.eurders());

        List<ItemGroup> itemGroups = request.eurders().stream()
                .map(order -> {
                    Item item = itemRepository.getOptionalById(order.itemId()).orElseThrow();
                    int amount = order.amount();

                    ItemGroup itemGroup = new ItemGroup(item, amount);
                    if (amount <= item.getAmount()) {
                        item.setAmount(item.getAmount() - amount);
                    }
                    return itemGroup;
                })
                .toList();

        return eurderRepository.save(new Eurder(itemGroups, userRepository.getUserWithEmail(user).get()));
    }

    private void checkOrdersValidity(List<PostEurderRequest.OrderLineDto> orders) {
        if (!orders.stream().allMatch(order -> order.amount() > 0))
            throw BadRequestException.invalidAmount(0);
        if (!orders.stream().allMatch(order -> itemRepository.getOptionalById(order.itemId()).isPresent()))
            throw BadRequestException.nonExistingItem("");
    }
}
