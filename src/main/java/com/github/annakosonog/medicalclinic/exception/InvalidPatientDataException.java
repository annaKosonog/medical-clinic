package com.github.annakosonog.medicalclinic.exception;

public class InvalidPatientDataException extends PatientException {
    public InvalidPatientDataException(String message) {
        super("Invalid patient data");
    }
}
