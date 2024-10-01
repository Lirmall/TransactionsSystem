package ru.klokov.tscommon.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.dtos.ReportTransactionDto;
import ru.klokov.tscommon.dtos.TransactionDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private PagedResult<ReportTransactionDto> transactionDtoPage;
}
