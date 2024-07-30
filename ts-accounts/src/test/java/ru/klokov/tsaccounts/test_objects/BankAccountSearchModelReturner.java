package ru.klokov.tsaccounts.test_objects;

import ru.klokov.tsaccounts.specifications.SearchCriteria;
import ru.klokov.tsaccounts.specifications.bank_account.BankAccountSearchModel;

import java.util.List;

public class BankAccountSearchModelReturner {
    public static BankAccountSearchModel returnModelWithOneId() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("id");
        criteria.setOperation(":");
        criteria.setFieldValue(1);

        List<SearchCriteria> criteriaList = List.of(criteria);

        return new BankAccountSearchModel(criteriaList);
    }
}
