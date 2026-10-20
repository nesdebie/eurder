package org.example.eurder.api.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.eurder.api.dto.eurder.EurderDto;
import org.example.eurder.api.dto.eurder.report.EurderReportDto;
import org.example.eurder.api.dto.eurder.report.EurdersReportDto;
import org.example.eurder.api.mapper.EurderMapper;
import org.example.eurder.api.mapper.EurdersReportMapper;
import org.example.eurder.api.request.PostEurderRequest;
import org.example.eurder.domain.report.EurdersReport;
import org.example.eurder.domain.user.Permission;
import org.example.eurder.service.EurderService;
import org.example.eurder.service.SecurityService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/eurder")
public class EurderController {
    private final EurderMapper mapper;
    private final EurderService service;
    private final SecurityService security;
    private final EurdersReportMapper eurdersReportMapper;

    public EurderController(EurderMapper mapper, EurderService service, SecurityService security, EurdersReportMapper eurdersReportMapper) {
        this.mapper = mapper;
        this.service = service;
        this.security = security;
        this.eurdersReportMapper = eurdersReportMapper;
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
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EurdersReportDto getEurdersReport(
            @Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String email)
    {
        security.validateAuthorization(email, Permission.VIEW_REPORT);
        return eurdersReportMapper.mapToDto(service.getEurdersReport(email));
    }
}
