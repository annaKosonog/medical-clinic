package com.github.annakosonog.medicalclinic.exception.doctor;

public class InvalidDoctorException extends DoctorException {
    public InvalidDoctorException(String message) {
        super("Invalid doctor data");
    }
}
