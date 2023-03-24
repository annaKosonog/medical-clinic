package com.github.annakosonog.medicalclinic.handler;

import com.github.annakosonog.medicalclinic.exception.doctor.DoctorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DoctorExceptionHandler {

    @ExceptionHandler(DoctorException.class)
    public ResponseEntity<String> doctorExceptionResponseError(DoctorException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
