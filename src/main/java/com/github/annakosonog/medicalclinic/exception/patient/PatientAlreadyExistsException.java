package com.github.annakosonog.medicalclinic.exception.patient;

public class PatientAlreadyExistsException extends PatientException {
    public PatientAlreadyExistsException() {
        super("Patient already exists");
    }
}
