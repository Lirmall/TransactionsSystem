package ru.klokov.tstransactions.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ru.klokov.tssystem.accounts.url}")
    private String url;
    private final RestTemplate restTemplate;

    public boolean verifyBankAccount(Long id) {
        VerificationResponse response = restTemplate.postForObject(url + "/verifyId", id, VerificationResponse.class);
        return response != null && response.getStatus().is2xxSuccessful();
    }

    public boolean checkBalanceForTransaction(Long recipientId, Double amount) {
        BankAccountBalanceVerificationDto dto = new BankAccountBalanceVerificationDto(recipientId, amount);
        VerificationResponse response = restTemplate.postForObject(url + "/verifyBalance",
                new VerificationBalanceRequest(dto),
                VerificationResponse.class);

        return response != null && response.getStatus().is2xxSuccessful();
    }

    public Boolean doTransaction(TransactionDataDto dto) {
        VerificationResponse response = restTemplate.postForObject(url + "/transaction",
                new TransactionRequest(dto), VerificationResponse.class);
        return response != null && response.getStatus().is2xxSuccessful();
    }
}
