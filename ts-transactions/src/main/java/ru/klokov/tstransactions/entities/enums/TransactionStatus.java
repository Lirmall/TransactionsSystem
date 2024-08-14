package ru.klokov.tstransactions.entities.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum TransactionStatus {
    SUCCESS(1L, "Success"),
    IN_PROGRESS(2L, "In Progress"),
    FAILED(3L, "Failed"),
    CANCELLED(4L, "Cancelled"),
    PENDING_CONFIRMATION(5L, "Pending Confirmation");

    private final Long id;
    private final String name;

    public static TransactionStatus getById(Long id) {
        return Stream.of(TransactionStatus.values())
                .filter(status -> status.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный статус транзакции: " + id));
    }

    public static TransactionStatus getByName(String name) {
        return Stream.of(TransactionStatus.values())
                .filter(status -> status.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный статус транзакции: " + name));
    }
}
