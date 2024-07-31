package ru.klokov.tsaccounts.specifications.bank_account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.klokov.tsaccounts.specifications.BaseSearchModel;
import ru.klokov.tsaccounts.specifications.SearchCriteria;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BankAccountSearchModel extends BaseSearchModel {
    private List<SearchCriteria> criteriaList;
}