package com.github.annakosonog.medicalclinic.model;

import java.util.Collections;

public interface SampleFacilityDto {

    default FacilityDto aMedicusDto() {
        return FacilityDto.builder()
                .name("Medicus")
                .city("Warszawa")
                .postCode("00-001")
                .street("Familijna")
                .number("103")
                .doctorId(null)
                .build();
    }

    default FacilityDto saveAMedicusDto() {
        return FacilityDto.builder()
                .name("Medicus")
                .city("Warszawa")
                .postCode("00-001")
                .street("Familijna")
                .number("103")
                .doctorId(Collections.singleton(1L))
                .build();
    }
}
