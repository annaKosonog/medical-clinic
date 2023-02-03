package com.github.annakosonog.medicalclinic.mapper;

import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import org.springframework.stereotype.Component;

@Component
public class PatientMapperImpl implements PatientMapper {
    @Override
    public PatientDTO patientToPatientDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        return PatientDTO.builder()
                .email(patient.getEmail())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .build();
    }
}
