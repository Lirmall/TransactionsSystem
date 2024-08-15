package ru.klokov.tsaccounts.controllers.handler;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.klokov.tsaccounts.dtos.ExceptionDto;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;
import ru.klokov.tsaccounts.exceptions.VerificationException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleAlreadyCreatedException(AlreadyCreatedException ex) {
        return this.buildResponseEntity(HttpStatus.CONFLICT, "AlreadyCreatedException", ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleVerificationException(VerificationException ex) {
        return this.buildResponseEntity(HttpStatus.CONFLICT, "VerificationException", ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleValidationException(ValidationException ex) {
        return this.buildResponseEntity(HttpStatus.CONFLICT, "ValidationException", ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionDto> handleRuntimeException(RuntimeException ex) {
        return this.buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "RuntimeException", ex.getMessage());
    }

    private ResponseEntity<ExceptionDto> buildResponseEntity(HttpStatus status, String exceptionName, String message) {
        LocalDateTime exceptionTime = LocalDateTime.now();

        log.error("{} {}", exceptionTime, message);

        ExceptionDto dto = new ExceptionDto(exceptionTime, status.value(), exceptionName, message);

        return new ResponseEntity<>(dto, status);
    }
}
