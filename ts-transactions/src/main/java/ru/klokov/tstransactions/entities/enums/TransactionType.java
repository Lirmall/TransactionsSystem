package ru.klokov.tstransactions.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TransactionType {
    PAYMENT(1L, "Payment"),
    DEPOSIT(2L, "Deposit"),
    CASH_WITHDRAWAL(3L, "Cash Withdrawal"),
    TRANSFER(4L, "Transfer");

    private final Long id;
    private final String name;

    public static TransactionType getById(Long id) {
        return Stream.of(TransactionType.values())
                .filter(status -> status.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный тип транзакции: " + id));
    }

    public static TransactionType getByName(String name) {
        return Stream.of(TransactionType.values())
                .filter(status -> status.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный тип транзакции: " + name));
    }
}
