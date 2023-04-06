package com.github.annakosonog.medicalclinic.model;

public interface SampleAssignDoctorDto {
    default AssignDoctorDto aCreateDoctorAdamBeforeSave() {
        return AssignDoctorDto.builder()
                .email("adam@wp.pl")
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(Specialization.OPHTHALMOLOGY)
                .build();
    }

    default AssignDoctorDto doctorPawelAlreadySaveToDb() {
        return AssignDoctorDto.builder()
                .email("pawel@wp.pl")
                .password("pawel123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .build();
    }

    default AssignDoctorDto aCreateANewDoctorWithNull() {
        return AssignDoctorDto.builder()
                .email(null)
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(null)
                .build();
    }

    default AssignDoctorDto aCreateDoctorPawelBeforeSave() {
        return AssignDoctorDto.builder()
                .email("pawel@wp.pl")
                .password("pawel123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .build();
    }

}
