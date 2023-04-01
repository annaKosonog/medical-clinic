package com.github.annakosonog.medicalclinic.model;

import java.util.Collections;
import java.util.Set;

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

    default FacilityDto aMedSanDto() {
        return FacilityDto.builder()
                .name("MedSan")
                .city("Warszawa")
                .postCode("00-002")
                .street("Kwiatowa")
                .number("2b")
                .doctorId(Collections.EMPTY_SET)
                .build();
    }

    default FacilityDto aMedSanDtoWithNull() {
        return FacilityDto.builder()
                .name("MedSan")
                .city(null)
                .postCode("00-002")
                .street(null)
                .number("2b")
                .doctorId(Collections.EMPTY_SET)
                .build();
    }

    default FacilityDto aMedSanDtoWithDoctor() {
        return FacilityDto.builder()
                .name("MedSan")
                .city("Warszawa")
                .postCode("00-002")
                .street("Kwiatowa")
                .number("2b")
                .doctorId(Set.of(1L, 2L))
                .build();
    }
}
