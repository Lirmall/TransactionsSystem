package ru.klokov.tsaccounts.test_objects;

import ru.klokov.tscommon.specifications.SearchCriteria;
import ru.klokov.tscommon.specifications.SearchOperation;
import ru.klokov.tscommon.specifications.search_models.BankAccountSearchModel;

import java.util.List;

public class BankAccountSearchModelReturner {
    public static BankAccountSearchModel returnModelWithOneId() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("id");
        criteria.setSearchOperation(SearchOperation.EQUALITY);
        criteria.setFieldValue(1);

        List<SearchCriteria> criteriaList = List.of(criteria);

        return new BankAccountSearchModel(criteriaList);
    }
    public static BankAccountSearchModel returnModelWithTwoIds() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("id");
        criteria.setSearchOperation(SearchOperation.EQUALITY);
        criteria.setFieldValue(1);
        criteria.setOrPredicate(true);

        SearchCriteria criteria2 = new SearchCriteria();
        criteria2.setFieldName("id");
        criteria2.setSearchOperation(SearchOperation.EQUALITY);
        criteria2.setFieldValue(2);
        criteria2.setOrPredicate(true);

        List<SearchCriteria> criteriaList = List.of(criteria, criteria2);

        return new BankAccountSearchModel(criteriaList);
    }

    public static BankAccountSearchModel returnModelWithBalanceMore() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("balance");
        criteria.setSearchOperation(SearchOperation.GREATER_THAN);
        criteria.setFieldValue(50000);

        List<SearchCriteria> criteriaList = List.of(criteria);

        BankAccountSearchModel model = new BankAccountSearchModel(criteriaList);
        model.setSortColumn("balance");

        return model;
    }

    public static BankAccountSearchModel returnModelWithWrongSortField() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("balance");
        criteria.setSearchOperation(SearchOperation.GREATER_THAN);
        criteria.setFieldValue(50000);

        List<SearchCriteria> criteriaList = List.of(criteria);

        BankAccountSearchModel model = new BankAccountSearchModel(criteriaList);
        model.setSortColumn("amount");

        return model;
    }

    public static BankAccountSearchModel returnModelWithBalanceMoreAndDESCSort() {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setFieldName("balance");
        criteria.setSearchOperation(SearchOperation.GREATER_THAN);
        criteria.setFieldValue(50000);

        List<SearchCriteria> criteriaList = List.of(criteria);

        BankAccountSearchModel model = new BankAccountSearchModel(criteriaList);
        model.setSortColumn("-id");

        return model;
    }
}
