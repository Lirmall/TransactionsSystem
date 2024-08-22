package ru.klokov.tsaccounts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.klokov.tsaccounts.dtos.BankAccountDto;
import ru.klokov.tsaccounts.services.BankAccountService;
import ru.klokov.tsaccounts.specifications.bank_account.BankAccountSearchModel;
import ru.klokov.tscommon.requests.TransactionRequest;
import ru.klokov.tscommon.requests.VerificationBalanceRequest;
import ru.klokov.tscommon.requests.VerificationRequest;
import ru.klokov.tscommon.requests.VerificationResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1/common/bank_accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @Operation(
            summary = "Verify bank account",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verifyId")
    public VerificationResponse verifyBankAccount(@RequestBody VerificationRequest request) {
        return new VerificationResponse(bankAccountService.verifyBankAccountById(request.getId()));
    }

    @Operation(
            summary = "Verify bank account balance for transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verifyBalance")
    public VerificationResponse verifyBalance(@RequestBody VerificationBalanceRequest request) {
        return new VerificationResponse(bankAccountService.verifyBankAccountBalanceById(request.getData()));
    }

    @Operation(
            summary = "Do transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/transaction")
    public VerificationResponse transaction(@RequestBody TransactionRequest request) {
        return new VerificationResponse(bankAccountService.doTransaction(request.getData()));
    }

    @Operation(
            summary = "Find bank account by search criteria",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/filter")
    public Page<BankAccountDto> findByFilter(@RequestBody BankAccountSearchModel model) {
        return bankAccountService.findByFilterWithCriteria(model);
    }
}
