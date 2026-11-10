package org.example.eurder.feature.eurder.api;

import org.example.eurder.common.api.AddressMapper;
import org.example.eurder.common.domain.Address;
import org.example.eurder.feature.eurder.api.dto.*;
import org.example.eurder.feature.eurder.api.request.EurderItemsRequest;
import org.example.eurder.feature.eurder.api.request.EurderSingleItemRequest;
import org.example.eurder.feature.eurder.domain.Eurder;
import org.example.eurder.feature.eurder.domain.ItemGroup;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EurderMapper {

    private final AddressMapper addressMapper;

    public EurderMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public EurderItemsRequest mapToEurderRequest(Eurder eurder) {
        return new EurderItemsRequest(
                eurder.getItemGroups().stream()
                        .map(this::mapToItemRequest).toList()
        );
    }

    public EurderSingleItemRequest mapToItemRequest(ItemGroup itemGroup) {
        return new EurderSingleItemRequest(
                itemGroup.itemId(),
                itemGroup.amount()
        );
    }

    public EurdersReportDto reportToDto(List<Eurder> eurders) {
        return new EurdersReportDto(
                eurders.stream().map(this::mapToDto).toList(),
                eurders.stream()
                        .map(Eurder::getTotalPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }


    public EurderDto mapToDto(Eurder eurder) {
        return new EurderDto(
                eurder.getId(),
                mapToDto(eurder.getItemGroups()),
                eurder.getTotalPrice()
        );
    }


    public ItemGroupDto mapToDto(ItemGroup itemGroup) {
        return new ItemGroupDto(
                itemGroup.itemId(),
                itemGroup.itemName(),
                itemGroup.unitPrice(),
                itemGroup.amount(),
                itemGroup.unitPrice().multiply(new BigDecimal(itemGroup.amount())),
                itemGroup.shippingDate()
        );
    }


    public List<ItemGroupDto> mapToDto(List<ItemGroup> itemGroups) {
        return itemGroups.stream().map(this::mapToDto).toList();
    }


    public ItemsShippedTodayDto mapToItemsShippedTodayDto(List<Eurder> eurders, LocalDate shippingDate) {
        Map<Address, List<ItemGroup>> itemGroupsByAddress = eurders.stream()
                .collect(Collectors.groupingBy(
                        eurder -> eurder.getCustomer().getAddress(),
                        LinkedHashMap::new,
                        Collectors.flatMapping(
                                eurder -> eurder.getItemGroupsShippingOn(shippingDate).stream(),
                                Collectors.toList())));

        return new ItemsShippedTodayDto(itemGroupsByAddress.entrySet().stream()
                .map(entry -> mapToDto(entry.getValue(), entry.getKey()))
                .toList());
    }

    public ItemGroupShippedTodayDto mapToDto(List<ItemGroup> groups, Address address) {
        return new ItemGroupShippedTodayDto(
                addressMapper.mapToDto(address),
                mapToDto(groups)
        );
    }
}
