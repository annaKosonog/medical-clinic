package com.github.annakosonog.medicalclinic.model;

public enum Role {

    PATIENT,
    ADMIN,
    DOCTOR;

    public String getAuthorityName() {
        return "ROLE_" + name();
    }
}
