package com.github.annakosonog.medicalclinic.exception;

public class PatientAlreadyExistsException extends PatientException {
    public PatientAlreadyExistsException() {
        super("Patient already exists");
    }
}
