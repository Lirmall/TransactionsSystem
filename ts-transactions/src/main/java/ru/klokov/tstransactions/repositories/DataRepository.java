package ru.klokov.tstransactions.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.klokov.tscommon.dtos.BankAccountBalanceVerificationDto;
import ru.klokov.tscommon.dtos.TransactionDataDto;
import ru.klokov.tscommon.requests.TransactionRequest;
import ru.klokov.tscommon.requests.VerificationBalanceRequest;
import ru.klokov.tscommon.requests.VerificationResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DataRepository {
    private static final String URL = "http://localhost:8089/api/v1/common/bank_accounts";
    private final RestTemplate restTemplate;


    public boolean verifyBankAccount(Long id) {
        VerificationResponse response = restTemplate.postForObject(URL + "/verifyId", id, VerificationResponse.class);
        return response != null && response.getIsValid();
    }

    public boolean checkBalanceForTransaction(Long recipientId, Double amount) {
        BankAccountBalanceVerificationDto dto = new BankAccountBalanceVerificationDto(recipientId, amount);
        VerificationResponse response = restTemplate.postForObject(URL + "/verifyBalance",
                new VerificationBalanceRequest(dto),
                VerificationResponse.class);

        return response != null && response.getIsValid();
    }

    public Boolean doTransaction(TransactionDataDto dto) {
        VerificationResponse response = restTemplate.postForObject(URL + "/transaction",
                new TransactionRequest(dto), VerificationResponse.class);
        return response != null && response.getIsValid();
    }
}
