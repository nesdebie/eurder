package org.example.eurder.api.mapper;

import org.example.eurder.api.dto.eurder.report.EurderReportDto;
import org.example.eurder.api.dto.eurder.report.EurdersReportDto;
import org.example.eurder.api.dto.eurder.report.ItemReportDto;
import org.example.eurder.domain.report.EurderReport;
import org.example.eurder.domain.report.EurdersReport;
import org.example.eurder.domain.report.ItemReport;
import org.springframework.stereotype.Component;

@Component
public class EurdersReportMapper {
    public EurdersReportDto mapToDto(EurdersReport report) {
        return new EurdersReportDto(
                report.getEurders().stream().map(this::mapToDto).toList(),
                report.getOrdersTotalPrice()
        );
    }

    public EurderReportDto mapToDto(EurderReport report) {
        return new EurderReportDto(
                report.getId(),
                report.getItems().stream().map(this::mapToDto).toList(),
                report.getTotalOrderPrice()
        );
    }

    public ItemReportDto mapToDto(ItemReport report) {
        return new ItemReportDto(
                report.getName(),
                report.getAmount(),
                report.getTotalItemPrice()
        );
    }
}
