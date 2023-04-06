package com.github.annakosonog.medicalclinic.handler;

import com.github.annakosonog.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MedicalClinicExceptionHandler {

    @ExceptionHandler(value = MedicalClinicException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> medicalClinicExceptionResponseError(MedicalClinicException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
