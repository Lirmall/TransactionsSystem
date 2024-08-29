package ru.klokov.tstransactions.specifications;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Schema(description = "Список критериев поиска транзакций")
public class TransactionSearchModel {
    private List<SearchCriteria> criteriaList;
}
