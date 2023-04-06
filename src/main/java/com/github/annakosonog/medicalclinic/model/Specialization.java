package com.github.annakosonog.medicalclinic.model;

import lombok.Getter;

@Getter
public enum Specialization {
    FAMILY_MEDICINE("Family_Medicine"),
    GYNECOLOGY("Gynecology"),
    UROLOGY("Urology"),
    CARDIOLOGY("Cardiology"),
    PSYCHIATRY("Psychiatry"),
    ORTHOPEDICS("Orthopedics"),
    OPHTHALMOLOGY("Ophthalmology");


    private String displayName;

    Specialization(String displayName) {
        this.displayName = displayName;
    }
}
