package ru.klokov.tsreports.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import ru.klokov.tscommon.dtos.*;
import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;
import ru.klokov.tscommon.specifications.search_models.BankAccountSearchModel;
import ru.klokov.tscommon.specifications.search_models.TransactionSearchModel;
import ru.klokov.tscommon.specifications.search_models.UserSearchModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GetReportsRepository {
    private static final String TRANSACTIONS_URL = "http://localhost:8090/api/v1/common/transactions";
    private static final String BANK_ACCOUNTS_URL = "http://localhost:8089/api/v1/common/bank_accounts";
    private static final String USERS_URL = "http://localhost:8089/api/v1/common/users";
    private static final String FILTER = "/filter";

    private final RestTemplate restTemplate;

    public PagedResult<TransactionDto> getTransactionsByPeriod(PeriodDto dto) {
        String transactionDate = "transactionDate";
        SearchCriteria criteria1 = new SearchCriteria(transactionDate, SearchOperation.GREATER_THAN, dto.getPeriodStart().toString(), false);
        SearchCriteria criteria2 = new SearchCriteria(transactionDate, SearchOperation.LESS_THAN, dto.getPeriodEnd().toString(), false);

        TransactionSearchModel searchModel = new TransactionSearchModel(List.of(criteria1, criteria2));
        searchModel.setSortColumn(transactionDate);

        ParameterizedTypeReference<PagedResult<TransactionDto>> typeReference = new ParameterizedTypeReference<>() {};

        PagedResult<TransactionDto> content =
                restTemplate.exchange(TRANSACTIONS_URL + FILTER, HttpMethod.POST, new HttpEntity<>(searchModel),typeReference).getBody();
        assert content != null;

        return content;
    }

    public PagedResult<BankAccountDto> getBankAccounts(Collection<Long> bankAccountIds) {
        List<SearchCriteria> criteria = new ArrayList<>();
        bankAccountIds.forEach(id -> criteria.add(new SearchCriteria("id", SearchOperation.EQUALITY, id, true)));

        BankAccountSearchModel model = new BankAccountSearchModel(criteria);

        ParameterizedTypeReference<PagedResult<BankAccountDto>> typeReference = new ParameterizedTypeReference<>() {};

        PagedResult<BankAccountDto> result =
                restTemplate.exchange(BANK_ACCOUNTS_URL + FILTER, HttpMethod.POST, new HttpEntity<>(model), typeReference).getBody();
        assert result != null;

        return result;
    }

    public PagedResult<UserDto> getUsersByIds(Collection<Long> ids) {
        UserSearchModel model = new UserSearchModel();
        model.setIds(ids.stream().toList());

        ParameterizedTypeReference<PagedResult<UserDto>> typeReference = new ParameterizedTypeReference<>() {};

        PagedResult<UserDto> result =
                restTemplate.exchange(USERS_URL + FILTER, HttpMethod.POST, new HttpEntity<>(model), typeReference).getBody();
        assert result != null;

        return result;
    }
}
