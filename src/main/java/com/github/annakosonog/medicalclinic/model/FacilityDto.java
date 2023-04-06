package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class FacilityDto {
    private String name;
    private String city;
    private String postCode;
    private String street;
    private String number;
    private Set<Long> doctorId;
}
