package ru.klokov.tsaccounts.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountModel {
    private Long id;
    private Long ownerUserId;
    private Double balance;
    private Boolean blocked;
    private Boolean deleted;
}
