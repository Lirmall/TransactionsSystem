package ru.klokov.tsaccounts.exceptions;

public class NoMatchingEntryInDatabaseException extends RuntimeException{
    public NoMatchingEntryInDatabaseException(String message) {
        super(message);
    }
}
