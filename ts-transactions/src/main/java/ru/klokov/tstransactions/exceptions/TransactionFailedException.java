package ru.klokov.tstransactions.exceptions;

public class TransactionFailedException extends RuntimeException{
    public  TransactionFailedException (String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionFailedException(String message) {
        super(message);
    }
}
