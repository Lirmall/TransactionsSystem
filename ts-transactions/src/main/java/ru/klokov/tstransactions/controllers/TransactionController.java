package ru.klokov.tstransactions.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;
import ru.klokov.tstransactions.mappers.TransactionMapper;
import ru.klokov.tstransactions.services.TransactionsService;

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
    public PagedResult<TransactionDto> findByFilter(@RequestBody TransactionSearchModel model) {
        Page<TransactionDto> dtos = transactionsService.findByFilterWithCriteria(model);
        return new PagedResult<>(dtos);
    }
}
