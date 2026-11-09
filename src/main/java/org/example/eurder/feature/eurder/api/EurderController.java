package org.example.eurder.feature.eurder.api;

import jakarta.validation.Valid;
import org.example.eurder.feature.eurder.api.dto.EurderDto;
import org.example.eurder.feature.eurder.api.dto.EurdersReportDto;
import org.example.eurder.feature.eurder.api.dto.ItemsShippedTodayDto;
import org.example.eurder.feature.eurder.api.request.EurderItemsRequest;
import org.example.eurder.feature.eurder.service.EurderService;
import org.example.eurder.feature.security.authentication.AuthenticatedUser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class EurderController {

    private final EurderService eurderService;

    public EurderController(EurderService eurderService) {
        this.eurderService = eurderService;
    }

    @PostMapping
    @Secured("EURDER_ITEMS")
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto createEurder(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody EurderItemsRequest request) {
        return eurderService.createEurder(request, user.id());
    }


    @PostMapping(value = "/{id}/reorder")
    @Secured("REEURDER_ITEMS")
    @ResponseStatus(HttpStatus.CREATED)
    public EurderDto createReeurder(
            @AuthenticationPrincipal AuthenticatedUser user,
            @PathVariable UUID id) {
        return eurderService.createReeurder(id, user.id());
    }


    @GetMapping("/report")
    @Secured("VIEW_EURDERS_REPORT")
    @ResponseStatus(HttpStatus.OK)
    public EurdersReportDto viewEurdersReport(
            @AuthenticationPrincipal AuthenticatedUser user) {
        return eurderService.viewEurdersReport(user.id());
    }

    @GetMapping("/shipped-today")
    @Secured("VIEW_SHIPPING_TODAY")
    @ResponseStatus(HttpStatus.OK)
    public ItemsShippedTodayDto viewItemsShippedToday() {
        return eurderService.viewItemsShippedToday();
    }
}
