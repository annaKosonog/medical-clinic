package com.github.annakosonog.medicalclinic.model;

import java.util.Collections;
import java.util.Set;

public interface SampleFacility {
    default Facility allParameterWithId(Long id, String name, String city, String postCode, String street, String number, Set<Doctor> doctors) {
        return new Facility(id, name, city, postCode, street, number, doctors);
    }

    default Facility allParametersWithoutId(String name, String city, String postCode, String street, String number, Set<Doctor> doctors) {
        final Facility facility = new Facility();
        facility.setName(name);
        facility.setCity(city);
        facility.setPostCode(postCode);
        facility.setStreet(street);
        facility.setNumber(number);
        facility.setDoctors(doctors);
        return facility;
    }

    default Facility beforeSaveMedicus() {
        return allParametersWithoutId(
                "Medicus",
                "Warszawa",
                "00-001",
                "Familijna",
                "103",
                Collections.EMPTY_SET);
    }

    default Facility afterSavedMedicus() {
        return allParameterWithId(
                1L,
                "Medicus",
                "Warszawa",
                "00-001",
                "Familijna",
                "103",
                Collections.EMPTY_SET);
    }

    default Facility createAMedSanBefore() {
        return allParametersWithoutId(
                "MedSan",
                "Warszawa",
                "00-002",
                "Kwiatowa",
                "2b",
                Collections.EMPTY_SET);
    }

    default Facility createAMedSanSavedToDb() {
        return allParameterWithId(
                2L,
                "MedSan",
                "Warszawa",
                "00-002",
                "Kwiatowa",
                "2b",
                Collections.EMPTY_SET);
    }

    default Facility aMedicusWithDoctorFacility() {
        return allParameterWithId(
                1L,
                "Medicus",
                "Warszawa",
                "00-001",
                "Familijna",
                "103",
                Collections.singleton(
                        Doctor.builder()
                                .id(2L)
                                .email("adam@wp.pl")
                                .password("adam123")
                                .firstName("Adam")
                                .lastName("Nowak")
                                .specialization(Specialization.OPHTHALMOLOGY)
                                .facilities(null)
                                .build()));
    }
}
