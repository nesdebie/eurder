package org.example.eurder.service;

import org.example.eurder.api.request.PostEurderRequest;
import org.example.eurder.domain.Eurder;
import org.example.eurder.domain.Item;
import org.example.eurder.domain.ItemGroup;
import org.example.eurder.domain.ItemToShipToday;
import org.example.eurder.domain.report.EurdersReport;
import org.example.eurder.repository.EurderRepository;
import org.example.eurder.repository.ItemRepository;
import org.example.eurder.repository.UserRepository;
import org.example.eurder.service.exception.BadRequestException;
import org.example.eurder.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<ItemToShipToday> getAllItemsShippedToday() {
        LocalDate today = LocalDate.now();

        return eurderRepository.getAll().stream()
                .flatMap(eurder -> eurder.getItems().stream()
                        .filter(group -> group.getShippingDate().equals(today))
                        .map(group -> new ItemToShipToday(group, eurder.getUser().getAddress()))
                )
                .toList();
    }

    public Eurder reeurder(UUID id, String email) {
        Eurder old = eurderRepository.get(id)
                .orElseThrow(() -> NotFoundException.eurderNotFound(id));

        List<PostEurderRequest.OrderLineDto> lines = old.getItems().stream()
                .map(group -> new PostEurderRequest.OrderLineDto(group.getItemId(), group.getAmount()))
                .toList();

        return createEurder(new PostEurderRequest(lines), email);
    }

    public Eurder createEurder(PostEurderRequest request, String email) {
        checkOrdersValidity(request.eurders());

        List<ItemGroup> itemGroups = request.eurders().stream()
                .map(order -> {
                    Item item = itemRepository.getOptionalById(order.itemId())
                            .orElseThrow();
                    int amount = order.amount();

                    ItemGroup itemGroup = new ItemGroup(item, amount);
                    if (amount <= item.getAmount()) {
                        item.setAmount(item.getAmount() - amount);
                    }
                    return itemGroup;
                })
                .toList();

        return eurderRepository.save(
                new Eurder(
                        itemGroups,
                        userRepository.getUserWithEmail(email).get()
                )
        );
    }

    private void checkOrdersValidity(List<PostEurderRequest.OrderLineDto> orders) {
        if (!orders.stream().allMatch(order -> order.amount() > 0))
            throw BadRequestException.invalidAmount(0);
        if (orders.stream().allMatch(order -> itemRepository.getOptionalById(order.itemId()).isEmpty()))
            throw BadRequestException.nonExistingItem("");
    }

    public EurdersReport getEurdersReport(String email) {
        return new EurdersReport(
                eurderRepository.getAll().stream()
                        .filter(eurder -> eurder.getUser().getEmail().equals(email))
                        .toList()
        );
    }
}
