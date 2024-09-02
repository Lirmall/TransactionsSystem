package ru.klokov.tscommon.exceptions;

public class NoMatchingEntryInDatabaseException extends RuntimeException{
    public NoMatchingEntryInDatabaseException(String message) {
        super(message);
    }
}
