package ru.klokov.tscommon.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.klokov.tscommon.dtos.BankAccountBalanceVerificationDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationBalanceRequest {
    private BankAccountBalanceVerificationDto data;
}
