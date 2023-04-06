package com.github.annakosonog.medicalclinic.model;

import java.util.Collections;

public interface SampleDoctorDto {

    default DoctorDto createAPawelDoctorDto() {
        return DoctorDto.builder()
                .email("pawel@wp.pl")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilitiesId(Collections.singleton(1L))
                .build();
    }
}
