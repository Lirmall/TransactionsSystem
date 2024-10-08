package ru.klokov.tstransactions.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.dtos.PeriodDto;
import ru.klokov.tscommon.dtos.ReportTransactionDto;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.requests.TransactionResponse;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.services.TransactionsService;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/common/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionsService transactionsService;
    private final TransactionMapper transactionMapper;

    @Operation(
            summary = "Create new transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public TransactionDto create(@RequestBody TransactionDto transactionDto) {
        return transactionMapper.convertModelToDto(transactionsService.create(transactionDto));
    }

    @Operation(
            summary = "Create new transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public TransactionDto findById(@PathVariable("id") UUID id) {
        return transactionMapper.convertModelToDto(transactionsService.findById(id));
    }

    @Operation(
            summary = "Create new transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/filter")
    public TransactionResponse findByFilter(@RequestBody TransactionSearchModel model) {
        PageImpl<TransactionDto> dtos = transactionsService.findByFilterWithCriteria(model);
        Page<ReportTransactionDto> content = dtos.map(transactionMapper::convertTransDTOToRepTransDTO);
        return new TransactionResponse(new PagedResult<>(content));
    }

    @Operation(
            summary = "Create new transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/byPeriod")
    public TransactionResponse findByPeriod(@RequestBody PeriodDto dto) {
        SearchCriteria criteria1 = new SearchCriteria("transactionDate", SearchOperation.GREATER_THAN, dto.getPeriodStart(), false);
        SearchCriteria criteria2 = new SearchCriteria("transactionDate", SearchOperation.LESS_THAN, dto.getPeriodEnd(), false);

        TransactionSearchModel searchModel = new TransactionSearchModel(List.of(criteria1, criteria2));
        searchModel.setSortColumn("transactionDate");

        PageImpl<TransactionDto> dtos = transactionsService.findByFilterWithCriteria(searchModel);

        List<ReportTransactionDto> content = dtos.getContent().stream().map(transactionMapper::convertTransDTOToRepTransDTO).toList();

        return new TransactionResponse(new PagedResult<>(content, dtos.getTotalPages(), dtos.getTotalElements(), dtos.getSize(), dtos.getNumber()));
    }
}
