package ru.klokov.tstransactions.test_objects;

import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;
import ru.klokov.tstransactions.specifications.TransactionSearchModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionSearchModelReturner {

    public static TransactionSearchModel returnModelWithOneId() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("id");
        criteria.setSearchOperation(SearchOperation.EQUALITY);
        criteria.setFieldValue(UUID.fromString("65833767-1827-4e1e-85ad-c7224290b798"));

        List<SearchCriteria> criteriaList = List.of(criteria);

        return new TransactionSearchModel(criteriaList);
    }

    public static TransactionSearchModel returnModelWithTwoDates() {
        SearchCriteria criteria1 = new SearchCriteria();
        criteria1.setFieldName("transactionDate");
        criteria1.setSearchOperation(SearchOperation.GREATER_THAN);
        criteria1.setFieldValue(LocalDateTime.of(2024, 8, 27, 0, 0, 0));

        SearchCriteria criteria2 = new SearchCriteria();
        criteria2.setFieldName("transactionDate");
        criteria2.setSearchOperation(SearchOperation.LESS_THAN);
        criteria2.setFieldValue(LocalDateTime.of(2024, 8, 30, 0, 0, 0));

        List<SearchCriteria> criteriaList = List.of(criteria1, criteria2);

        TransactionSearchModel model = new TransactionSearchModel(criteriaList);
        model.setSortColumn("-transactionDate");

        return model;
    }
}
