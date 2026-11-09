package org.example.eurder.feature.eurder.service;

import org.example.eurder.feature.customer.service.CustomerService;
import org.example.eurder.feature.eurder.api.EurderMapper;
import org.example.eurder.feature.eurder.api.dto.EurderDto;
import org.example.eurder.feature.eurder.api.dto.EurdersReportDto;
import org.example.eurder.feature.eurder.api.dto.ItemsShippedTodayDto;
import org.example.eurder.feature.eurder.api.request.EurderItemsRequest;
import org.example.eurder.feature.eurder.domain.Eurder;
import org.example.eurder.feature.eurder.domain.EurderRepository;
import org.example.eurder.feature.eurder.domain.ItemGroup;
import org.example.eurder.feature.eurder.exception.EurderForbiddenException;
import org.example.eurder.feature.eurder.exception.EurderNotFoundException;
import org.example.eurder.feature.item.domain.Item;
import org.example.eurder.feature.item.domain.ItemRepository;
import org.example.eurder.feature.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class EurderService {
    private final EurderRepository eurderRepository;
    private final ItemService itemService;
    private final Clock clock;
    private final EurderMapper eurderMapper;
    private final CustomerService customerService;


    public EurderService(EurderRepository eurderRepository, ItemService itemService, Clock clock, EurderMapper eurderMapper, CustomerService customerService) {
        this.eurderRepository = eurderRepository;
        this.itemService = itemService;
        this.clock = clock;
        this.eurderMapper = eurderMapper;
        this.customerService = customerService;
    }


    public EurderDto createEurder(EurderItemsRequest request, UUID customerId) {
        List<ItemGroup> itemGroups = request.items().stream()
                .map(itemToBuy -> {
                    Item item = itemService.getItemById(itemToBuy.id());

                    ItemGroup itemGroup = new ItemGroup(
                            item.getId(),
                            item.getName(),
                            item.getPrice(),
                            itemToBuy.amount(),
                            LocalDate.now(clock));
                    itemService.reduceStock(item.getId(), itemToBuy.amount());
                    return itemGroup;
                })
                .toList();

        Eurder eurder = new Eurder(
                customerService.getCustomerById(customerId),
                LocalDate.now(clock),
                itemGroups
                );

        eurderRepository.saveAndFlush(eurder);
        return eurderMapper.mapToDto(eurder);
    }

    public EurderDto createReeurder(UUID eurderId, UUID customerId) {
        Eurder eurder = getEurderById(eurderId);
        verifyCustomerIdAreMatching(eurder, customerId);
        return createEurder(eurderMapper.mapToEurderRequest(eurder), customerId);
    }

    public EurdersReportDto viewEurdersReport(UUID customerId) {
        return eurderMapper.reportToDto(
                eurderRepository.findAllByCustomer(
                        customerService
                                .getCustomerById(customerId)
                )
        );
    }

    public ItemsShippedTodayDto viewItemsShippedToday() {
        return eurderMapper.mapToItemsShippedTodayDto(
                eurderRepository.findAll(),
                LocalDate.now(clock)
        );
    }

    public Eurder getEurderById(UUID id) {
        return eurderRepository.findById(id)
                .orElseThrow(() -> EurderNotFoundException.eurderIdNotFound(id));
    }

    private void verifyCustomerIdAreMatching(Eurder eurder, UUID customerId) {
        if (eurder.getCustomer().getId() != customerId) throw EurderForbiddenException.forbiddenException();
    }
}
