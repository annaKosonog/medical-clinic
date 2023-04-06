package com.github.annakosonog.medicalclinic.mapper;

import com.github.annakosonog.medicalclinic.model.AssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DoctorMapper {

    DoctorMapper INSTANCE = Mappers.getMapper(DoctorMapper.class);

    @Mapping(target = "facilities", ignore = true)
    Doctor assignDoctorDtoToDoctor(AssignDoctorDto assignDoctorDto);
}
