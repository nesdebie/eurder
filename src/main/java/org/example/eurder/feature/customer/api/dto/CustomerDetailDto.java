package org.example.eurder.feature.customer.api.dto;

import org.example.eurder.feature.eurder.api.dto.EurdersReportDto;

public record CustomerDetailDto(
        CustomerDto customerDto,
        EurdersReportDto eurders
) {}
