package ru.klokov.tstransactions.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.klokov.tscommon.requests.VerificationRequest;
import ru.klokov.tscommon.requests.VerificationResponse;
import ru.klokov.tstransactions.dtos.TransactionDto;
import ru.klokov.tstransactions.exceptions.VerificationException;
import ru.klokov.tstransactions.services.TransactionsService;

@Slf4j
@RestController
@RequestMapping("/api/v1/common/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionsService transactionsService;

    @Operation(
            summary = "Create new transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public void create(@RequestBody TransactionDto transactionDto) {
        transactionsService.create(transactionDto);
    }
}
