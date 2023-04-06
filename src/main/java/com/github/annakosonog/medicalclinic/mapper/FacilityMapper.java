package com.github.annakosonog.medicalclinic.mapper;

import com.github.annakosonog.medicalclinic.model.Facility;
import com.github.annakosonog.medicalclinic.model.FacilityDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedSourcePolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FacilityMapper {

    FacilityMapper INSTANCE = Mappers.getMapper(FacilityMapper.class);

    @Mapping(target = "doctors", ignore = true)
    Facility facilityDtoToFacility(FacilityDto facilityDto);

}
