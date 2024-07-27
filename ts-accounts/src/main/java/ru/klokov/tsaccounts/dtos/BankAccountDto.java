package ru.klokov.tsaccounts.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDto {
    private Long id;
    private Long ownerUserId;
    private Double balance;
    private Boolean blocked;
    private Boolean deleted;
}
