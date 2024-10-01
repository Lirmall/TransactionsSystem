package ru.klokov.tsreports.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.dtos.PeriodDto;
import ru.klokov.tscommon.dtos.ReportTransactionDto;
import ru.klokov.tscommon.dtos.TransactionDto;
import ru.klokov.tscommon.requests.TransactionResponse;
import ru.klokov.tscommon.requests.VerificationResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetReportsRepository {
    private static final String TRANSACTIONS_URL = "http://localhost:8090/api/v1/common/transactions";
    private static final String BANK_ACCOUNTS_URL = "http://localhost:8089/api/v1/common/bank_accounts";
    private static final String USERS_URL = "http://localhost:8089/api/v1/common/users";
    private final RestTemplate restTemplate;

    public Page<ReportTransactionDto> getTransactionsByPeriod(PeriodDto dto) {
        TransactionResponse response = restTemplate.postForObject(TRANSACTIONS_URL + "/byPeriod", dto, TransactionResponse.class);
        assert response != null;

        PagedResult<ReportTransactionDto> content = response.getTransactionDtoPage();
        List<ReportTransactionDto> sortedContent = content.getContent().stream().sorted(Comparator.comparing(ReportTransactionDto::getTransactionDate)).toList();

        Sort sort = Sort.by(Sort.Direction.ASC, "transactionDate");
        Pageable pageable = PageRequest.of(content.getNumber(), content.getSize(), sort);

        return new PageImpl<>(sortedContent, pageable, content.getTotalElements());
    }
}
