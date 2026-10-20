package org.example.eurder.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.eurder.api.dto.ItemToShipTodayDto;
import org.example.eurder.api.dto.eurder.EurderDto;
import org.example.eurder.api.dto.eurder.report.EurdersReportDto;
import org.example.eurder.api.mapper.EurderMapper;
import org.example.eurder.api.mapper.EurdersReportMapper;
import org.example.eurder.api.mapper.ItemGroupMapper;
import org.example.eurder.api.mapper.ItemToShipTodayMapper;
import org.example.eurder.api.request.PostEurderRequest;
import org.example.eurder.domain.user.Permission;
import org.example.eurder.service.EurderService;
import org.example.eurder.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/eurder")
public class EurderController {
    private final EurderMapper mapper;
    private final EurderService service;
    private final SecurityService security;
    private final EurdersReportMapper eurdersReportMapper;
    private final ItemGroupMapper itemGroupMapper;
    private final ItemToShipTodayMapper toShipToday;

    public EurderController(EurderMapper mapper, EurderService service, SecurityService security, EurdersReportMapper eurdersReportMapper, ItemGroupMapper itemGroupMapper, ItemToShipTodayMapper toShipToday) {
        this.mapper = mapper;
        this.service = service;
        this.security = security;
        this.eurdersReportMapper = eurdersReportMapper;
        this.itemGroupMapper = itemGroupMapper;
        this.toShipToday = toShipToday;
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto createEurder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email,
            @RequestBody PostEurderRequest request)
    {
        security.validateAuthorization(email, Permission.ORDER_ITEM);
        return mapper.mapToDto(service.createEurder(request, email));
    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto reeurder(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email,
            @PathVariable UUID id)
    {
        security.validateAuthorization(email, Permission.ORDER_ITEM);
        return mapper.mapToDto(service.reeurder(id, email));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EurdersReportDto getEurdersReport(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email)
    {
        security.validateAuthorization(email, Permission.VIEW_REPORT);
        return eurdersReportMapper.mapToDto(service.getEurdersReport(email));
    }

    @SecurityRequirement(name = "Authorization")
    @GetMapping(value = "/today")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemToShipTodayDto> getAllItemsShippedToday(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email)
    {
        security.validateAuthorization(email, Permission.VIEW_SHIPPING);
        return toShipToday.mapToDto(service.getAllItemsShippedToday());
    }
}
