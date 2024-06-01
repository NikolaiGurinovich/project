package com.example.project.exeptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerGlobal {

    @ExceptionHandler(value = UserAlreadyExists.class)
    public ResponseEntity<HttpStatusCode> userAlreadyExists(Exception exception) {
        log.error("user already exists {0}", exception);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
