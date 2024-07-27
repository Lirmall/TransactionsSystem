package ru.klokov.tsaccounts.exceptions;

public class VerificationException extends RuntimeException{
    public VerificationException(String message) {
        super(message);
    }
}
