package com.github.annakosonog.medicalclinic.mapper;

import com.github.annakosonog.medicalclinic.model.Visit;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface VisitMapper {

    VisitMapper INSTANCE = Mappers.getMapper(VisitMapper.class);

    @Mapping(target = "patientId", source = "patient.id")
    VisitDTO visitToVisitDto(Visit visit);

    Visit visitDtoToVisit(VisitDTO visitDTO);


}