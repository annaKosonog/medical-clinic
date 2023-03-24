package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DoctorDto {
    private String email;
    private String firstName;
    private String lastName;
    private Specialization specialization;
    private List<Facility> facilities;
}
