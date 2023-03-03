package com.github.annakosonog.medicalclinic.handler;
import com.github.annakosonog.medicalclinic.exception.PatientException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PatientExceptionHandler {

    @ExceptionHandler(PatientException.class)
    public ResponseEntity<String> patientExceptionResponseError(PatientException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
