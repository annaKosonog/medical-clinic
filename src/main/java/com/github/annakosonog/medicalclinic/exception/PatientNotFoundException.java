package com.github.annakosonog.medicalclinic.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException() {
        super("Patient not found exception");
    }
}
