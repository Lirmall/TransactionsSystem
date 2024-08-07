package ru.klokov.tsaccounts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.klokov.tsaccounts.services.BankAccountService;
import ru.klokov.tscommon.requests.VerificationRequest;
import ru.klokov.tscommon.requests.VerificationResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/common/bank_accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Operation(
            summary = "Create new transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verifyId")
    public VerificationResponse verifyBankAccount(@RequestBody VerificationRequest request) {
        return new VerificationResponse(bankAccountService.verifyBankAccountById(request.getId()));
    }
}
