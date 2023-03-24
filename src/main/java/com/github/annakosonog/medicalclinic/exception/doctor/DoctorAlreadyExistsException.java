package com.github.annakosonog.medicalclinic.exception.doctor;

public class DoctorAlreadyExistsException extends DoctorException {
    public DoctorAlreadyExistsException() {
        super("Doctor already exists");
    }
}
