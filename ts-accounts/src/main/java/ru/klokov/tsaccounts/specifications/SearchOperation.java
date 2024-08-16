package ru.klokov.tsaccounts.specifications;

import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonValue
    public String getOperation() {
        return operation;
    }
}
