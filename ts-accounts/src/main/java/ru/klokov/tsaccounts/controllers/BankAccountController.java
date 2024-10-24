package ru.klokov.tsaccounts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.klokov.tsaccounts.dtos.CreateBankAccountDto;
import ru.klokov.tsaccounts.mappers.BankAccountMapper;
import ru.klokov.tsaccounts.models.BankAccountModel;
import ru.klokov.tsaccounts.services.BankAccountService;
import ru.klokov.tscommon.dtos.BankAccountDto;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.requests.TransactionRequest;
import ru.klokov.tscommon.requests.VerificationBalanceRequest;
import ru.klokov.tscommon.requests.VerificationRequest;
import ru.klokov.tscommon.requests.VerificationResponse;
import ru.klokov.tscommon.specifications.search_models.BankAccountSearchModel;

@Slf4j
@RestController
@RequestMapping("/api/v1/common/bank_accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final BankAccountMapper bankAccountMapper;

    @Operation(
            summary = "Verify bank account",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verifyId")
    public VerificationResponse verifyBankAccount(@RequestBody VerificationRequest request) {
        if(bankAccountService.verifyBankAccountById(request.getId())) {
            return new VerificationResponse(HttpStatus.OK);
        } else {
            return new VerificationResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Verify bank account balance for transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/verifyBalance")
    public VerificationResponse verifyBalance(@RequestBody VerificationBalanceRequest request) {
        if(bankAccountService.verifyBankAccountBalanceById(request.getData())) {
            return new VerificationResponse(HttpStatus.OK);
        } else {
            return new VerificationResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Do transaction",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/transaction")
    public VerificationResponse transaction(@RequestBody TransactionRequest request) {
        try {
            bankAccountService.doTransaction(request.getData());
            return new VerificationResponse(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new VerificationResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Find bank account by search criteria",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/filter")
    public PagedResult<BankAccountDto> findByFilter(@RequestBody BankAccountSearchModel model) {
        Page<BankAccountDto> dtos = bankAccountService.findByFilterWithCriteria(model);
        return new PagedResult<>(dtos);
    }

    @Operation(
            summary = "Create new bank account",
            method = "post")
    @ApiResponse(responseCode = "200", description = "Request successful")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping
    public BankAccountDto create(@RequestBody CreateBankAccountDto bankAccountDto) {
        log.debug("Try to create user bank account for user with id {}", bankAccountDto);
        BankAccountModel model = bankAccountService.create(bankAccountDto.getOwnerUserId());
        return bankAccountMapper.convertModelToDTO(model);
    }
}
