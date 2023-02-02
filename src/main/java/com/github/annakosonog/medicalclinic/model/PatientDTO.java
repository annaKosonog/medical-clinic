package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatientDTO {
    private String email;
    private String firstName;
    private String lastName;
}
