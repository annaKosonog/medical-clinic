package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class DoctorDto {
    private String email;
    private String firstName;
    private String lastName;
    private Specialization specialization;
    private Set<Long> facilitiesId;
}
