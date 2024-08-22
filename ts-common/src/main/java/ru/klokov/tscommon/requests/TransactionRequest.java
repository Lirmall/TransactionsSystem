package ru.klokov.tscommon.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.dtos.BankAccountBalanceVerificationDto;
import ru.klokov.tscommon.dtos.TransactionDataDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private TransactionDataDto data;
}
