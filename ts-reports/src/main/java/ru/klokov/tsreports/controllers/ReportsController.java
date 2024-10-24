package ru.klokov.tsreports.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.specifications.search_models.BankAccountSearchModel;
import ru.klokov.tsreports.dtos.ReportDto;
import ru.klokov.tsreports.mappers.ReportsMapper;
import ru.klokov.tsreports.services.ReportsService;

@RestController
@RequestMapping("/api/v1/common/reports")
@RequiredArgsConstructor
public class ReportsController {
    private final ReportsService reportsService;
    private final ReportsMapper reportsMapper;

    @Operation(
            summary = "Get all or new reports data to DB",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping()
    public void fillAllOrNewReportsToDB() {
        reportsService.fillAllOrNewReportsToDB();
    }

    @Operation(
            summary = "Find transactions by search criteria",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/filter")
    public PagedResult<ReportDto> findByFilter(@RequestBody BankAccountSearchModel model) {
        Page<ReportDto> dtos = reportsService.findByFilterWithCriteria(model);
        return new PagedResult<>(dtos);
    }
}
