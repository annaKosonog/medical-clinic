package com.github.annakosonog.medicalclinic.handler;

import com.github.annakosonog.medicalclinic.exception.MedicalClinicException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MedicalClinicExceptionHandler {

    @ExceptionHandler(value = MedicalClinicException.class)
    public ResponseEntity<String> medicalClinicExceptionResponseError(MedicalClinicException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
