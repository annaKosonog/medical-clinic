package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class AssignDoctorDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Specialization specialization;

    public void setPassword(String password) {
        this.password = password;
    }
}
