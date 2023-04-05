package com.github.annakosonog.medicalclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface SampleDoctor {

    default Doctor beforeSavingAPawel() {
        return Doctor.builder()
                .email("pawel@wp.pl")
                .password("pawel123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilities(null)
                .build();
    }

    default Doctor afterSavingAPawelToDb() {
        return Doctor.builder()
                .id(1L)
                .email("pawel@wp.pl")
                .password("pawel123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilities(null)
                .build();
    }

    default Doctor aPawelToDbWithMedicusFacility() {
        List<Facility> addFacilities = new ArrayList<>();
        addFacilities.add(new Facility(
                1L,
                "Medicus",
                "Warszawa",
                "00-001",
                "Familijna",
                "103",
                Collections.EMPTY_SET)
        );

        return Doctor.builder()
                .id(1L)
                .email("pawel@wp.pl")
                .password("pawel123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilities(addFacilities)
                .build();
    }


    default Doctor createAdam() {
        return Doctor.builder()
                .email("adam@wp.pl")
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(Specialization.OPHTHALMOLOGY)
                .facilities(null)
                .build();
    }

    default Doctor savedAdamToDb() {
        return Doctor.builder()
                .id(2L)
                .email("adam@wp.pl")
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(Specialization.OPHTHALMOLOGY)
                .facilities(null)
                .build();
    }
}
