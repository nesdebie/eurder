package org.example.eurder.feature.eurder.api;

import org.example.eurder.feature.eurder.api.dto.EurderDto;
import org.example.eurder.feature.eurder.api.dto.EurdersReportDto;
import org.example.eurder.feature.eurder.api.dto.ItemGroupDto;
import org.example.eurder.feature.eurder.api.request.EurderItemsRequest;
import org.example.eurder.feature.eurder.api.request.EurderSingleItemRequest;
import org.example.eurder.feature.eurder.domain.Eurder;
import org.example.eurder.feature.eurder.domain.ItemGroup;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class EurderMapper {
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
}
