package ru.klokov.tscommon.specifications.search_models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.specifications.BaseSearchModel;
import ru.klokov.tscommon.specifications.SearchCriteria;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "Список критериев поиска")
public class BankAccountSearchModel extends BaseSearchModel {
    private List<SearchCriteria> criteriaList;
}
