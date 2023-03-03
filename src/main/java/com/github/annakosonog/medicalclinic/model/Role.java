package com.github.annakosonog.medicalclinic.model;
public enum Role {

    PATIENT,
    ADMIN;

    public String getAuthorityName() {
        return "ROLE_" + name();
    }
}
