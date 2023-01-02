package com.github.annakosonog.medicalclinic.exception;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException() {
        super("Patient already exists");
    }
}
