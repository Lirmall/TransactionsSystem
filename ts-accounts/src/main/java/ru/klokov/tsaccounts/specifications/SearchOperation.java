package ru.klokov.tsaccounts.specifications;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchOperation {

    GREATER_THAN(">"),
    LESS_THAN("<"),
    EQUALITY(":"),
    LIKE("~");

    private final String operation;
}
