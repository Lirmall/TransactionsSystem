package ru.klokov.tsreports.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.klokov.tscommon.dtos.*;
import ru.klokov.tscommon.requests.BankAccountResponse;
import ru.klokov.tscommon.requests.TransactionResponse;
import ru.klokov.tscommon.requests.UserResponse;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;
import ru.klokov.tscommon.specifications.search_models.BankAccountSearchModel;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;
import ru.klokov.tscommon.specifications.search_models.UserSearchModel;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetReportsRepository {
    private static final String TRANSACTIONS_URL = "http://localhost:8090/api/v1/common/transactions";
    private static final String BANK_ACCOUNTS_URL = "http://localhost:8089/api/v1/common/bank_accounts";
    private static final String USERS_URL = "http://localhost:8089/api/v1/common/users";
    private static final String FILTER = "/filter";
    private final RestTemplate restTemplate;

    //Этот вариант с эндпоинтом на /filter надо доработать для обхода ошибки неверного каста String -> LocalDateTime
//    public Page<ReportTransactionDto> getTransactionsByPeriod(PeriodDto dto) {
//        String transactionDate = "transactionDate";
//        SearchCriteria criteria1 = new SearchCriteria(transactionDate, SearchOperation.GREATER_THAN, dto.getPeriodStart(), false);
//        SearchCriteria criteria2 = new SearchCriteria(transactionDate, SearchOperation.LESS_THAN, dto.getPeriodEnd(), false);
//
//        TransactionSearchModel searchModel = new TransactionSearchModel(List.of(criteria1, criteria2));
//        searchModel.setSortColumn(transactionDate);
//
//        TransactionResponse response = restTemplate.postForObject(TRANSACTIONS_URL + FILTER, searchModel, TransactionResponse.class);
//        assert response != null;
//
//        PagedResult<ReportTransactionDto> content = response.getTransactionDtoPage();
//        List<ReportTransactionDto> sortedContent = content.getContent().stream().sorted(Comparator.comparing(ReportTransactionDto::getTransactionDate)).toList();
//
//        Sort sort = Sort.by(Sort.Direction.ASC, transactionDate);
//        Pageable pageable = PageRequest.of(content.getNumber(), content.getSize(), sort);
//
//        return new PageImpl<>(sortedContent, pageable, content.getTotalElements());
//    }

    public Page<ReportTransactionDto> getTransactionsByPeriod(PeriodDto dto) {
        TransactionResponse response = restTemplate.postForObject(TRANSACTIONS_URL + "/byPeriod", dto, TransactionResponse.class);
        assert response != null;

        PagedResult<ReportTransactionDto> content = response.getTransactionDtoPage();
        List<ReportTransactionDto> sortedContent = content.getContent().stream().sorted(Comparator.comparing(ReportTransactionDto::getTransactionDate)).toList();

        Sort sort = Sort.by(Sort.Direction.ASC, "transactionDate");
        Pageable pageable = PageRequest.of(content.getNumber(), content.getSize(), sort);

        return new PageImpl<>(sortedContent, pageable, content.getTotalElements());
    }

    public List<ReportBankAccountDto> getBankAccounts(Collection<Long> bankAccountIds) {
        List<SearchCriteria> criteria = new ArrayList<>();
        bankAccountIds.forEach(id -> criteria.add(new SearchCriteria("id", SearchOperation.EQUALITY, id, true)));

        BankAccountSearchModel model = new BankAccountSearchModel(criteria);

        BankAccountResponse response = restTemplate.postForObject(BANK_ACCOUNTS_URL + FILTER, model, BankAccountResponse.class);
        assert response != null;

        return response.getBankAccountDtoPagedResult().getContent();
    }

    public List<ReportUserDto> getUsersByIds(Collection<Long> ids) {
        UserSearchModel model = new UserSearchModel();
        model.setIds(ids.stream().toList());

        UserResponse result = restTemplate.postForObject(USERS_URL + FILTER, model, UserResponse.class);
        assert result != null;

        return result.getUserDtoPagedResult().getContent();
    }
}
