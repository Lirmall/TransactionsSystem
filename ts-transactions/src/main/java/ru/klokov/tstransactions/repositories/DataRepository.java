package ru.klokov.tstransactions.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.klokov.tscommon.requests.VerificationResponse;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DataRepository {
    private static final String URL = "http://localhost:8089/api/v1/common/bank_accounts";
    private final RestTemplate restTemplate;
    private final String verifyBankAccountBalance = URL + "/verifyBalance";


    public boolean verifyBankAccount(Long id) {
        VerificationResponse response = restTemplate.postForObject(URL + "/verifyId", id, VerificationResponse.class);
        return response != null && response.getIsValid();
    }

    public boolean checkBalanceForTransaction(Long recipientId, Double amount) {
        return false;
    }
}
