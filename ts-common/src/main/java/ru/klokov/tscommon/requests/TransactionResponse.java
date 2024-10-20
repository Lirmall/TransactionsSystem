package ru.klokov.tscommon.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.dtos.PagedResult;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse<T> {
    private PagedResult<T> transactionDtoPage;
}
