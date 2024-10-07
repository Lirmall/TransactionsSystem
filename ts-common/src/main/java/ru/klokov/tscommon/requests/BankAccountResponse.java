package ru.klokov.tscommon.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.dtos.PagedResult;
import ru.klokov.tscommon.dtos.ReportBankAccountDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {
    private PagedResult<ReportBankAccountDto> bankAccountDtoPagedResult;
}
