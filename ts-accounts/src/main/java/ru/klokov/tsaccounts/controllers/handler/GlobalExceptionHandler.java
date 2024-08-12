package ru.klokov.tsaccounts.controllers.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.klokov.tsaccounts.exceptions.AlreadyCreatedException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleAlreadyCreatedException(AlreadyCreatedException ex) {
        log.error(String.format("%s %s", LocalDateTime.now(), ex.getMessage()));
        return new ResponseEntity<>(String.format("%s %s", LocalDateTime.now(), ex.getMessage()), HttpStatus.CONFLICT);
    }
 }
