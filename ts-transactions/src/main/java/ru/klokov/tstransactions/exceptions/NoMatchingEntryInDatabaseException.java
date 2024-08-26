package ru.klokov.tstransactions.exceptions;

public class NoMatchingEntryInDatabaseException extends RuntimeException{
    public NoMatchingEntryInDatabaseException(String message) {
        super(message);
    }
}
