package com.github.annakosonog.medicalclinic.exception.patient;

public class PatientNotFoundException extends PatientException {

    public PatientNotFoundException() {
        super("Patient not found");
    }
}
