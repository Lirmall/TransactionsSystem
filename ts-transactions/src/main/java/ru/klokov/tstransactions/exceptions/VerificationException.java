package ru.klokov.tstransactions.exceptions;

public class VerificationException extends RuntimeException{
    public VerificationException(String message) {
        super(message);
    }
}