package com.github.annakosonog.medicalclinic.exception.patient;

public class InvalidPatientDataException extends PatientException {
    public InvalidPatientDataException(String message) {
        super("Invalid patient data");
    }
}
