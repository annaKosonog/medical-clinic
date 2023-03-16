package com.github.annakosonog.medicalclinic.handler;

import com.github.annakosonog.medicalclinic.exception.visit.VisitException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VisitExceptionHandler {

    @ExceptionHandler(VisitException.class)
    public ResponseEntity<String> visitExceptionResponseError(VisitException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
