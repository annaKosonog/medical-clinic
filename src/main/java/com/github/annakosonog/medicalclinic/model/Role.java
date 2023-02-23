package com.github.annakosonog.medicalclinic.model;

public enum Role {

    PATIENT;

    public String getAuthorityName() {
        return "ROLE_" + name();
    }
}
