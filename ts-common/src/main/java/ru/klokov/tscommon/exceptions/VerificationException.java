package ru.klokov.tscommon.exceptions;

public class VerificationException extends RuntimeException{
    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationException(String message) {
        super(message);
    }
}
