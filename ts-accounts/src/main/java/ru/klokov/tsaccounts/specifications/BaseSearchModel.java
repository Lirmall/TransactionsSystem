package ru.klokov.tsaccounts.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BaseSearchModel {
    private Integer pages = 0;
    private Integer limit = 5;
    private String sortColumn;
}
