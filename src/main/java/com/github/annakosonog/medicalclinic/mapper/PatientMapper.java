package com.github.annakosonog.medicalclinic.mapper;

import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {

    PatientDTO patientToPatientDto(Patient patient);
}
