package ru.klokov.tsreports.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.klokov.tscommon.dtos.PeriodDto;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tsreports.mappers.ReportsMapper;
import ru.klokov.tsreports.services.ReportsService;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/common/reports")
@RequiredArgsConstructor
public class ReportsController {
    private final ReportsService reportsService;
    private final ReportsMapper reportsMapper;

    @Operation(
            summary = "Get all or new data to DB",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping()
    public void fillReportsToDB() {
        reportsService.fillReportsToDB(new PeriodDto(LocalDateTime.of(1970, 1, 1, 0, 0), LocalDateTime.now()));
    }
}
