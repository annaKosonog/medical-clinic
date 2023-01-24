package com.github.annakosonog.medicalclinic.exception;

public class PatientNotFoundException extends PatientException {

    public PatientNotFoundException() {
        super("Patient not found");
    }
}
