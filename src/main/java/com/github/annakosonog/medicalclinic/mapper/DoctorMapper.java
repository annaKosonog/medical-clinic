package com.github.annakosonog.medicalclinic.mapper;

import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    DoctorDto doctorToDoctorDto(Doctor doctor);
}
