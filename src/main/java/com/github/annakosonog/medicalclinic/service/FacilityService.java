package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.facility.FacilityAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.facility.InvalidFacilityDataException;
import com.github.annakosonog.medicalclinic.mapper.FacilityMapper;
import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.Facility;
import com.github.annakosonog.medicalclinic.model.FacilityDto;
import com.github.annakosonog.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public List<FacilityDto> findAllFacility() {
        return facilityRepository.findAll()
                .stream()
                .map(facility -> FacilityDto.builder()
                        .name(facility.getName())
                        .city(facility.getCity())
                        .postCode(facility.getPostCode())
                        .street(facility.getStreet())
                        .number(facility.getNumber())
                        .doctorId(facility.getDoctors().stream().map(Doctor::getId).collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }

    public void createANewFacility(FacilityDto facilityDto) {
        final Optional<Facility> name = facilityRepository.findByName(facilityDto.getName());
        if (name.isPresent()) {
            throw new FacilityAlreadyExistsException();
        }
        if (!isValid(facilityDto)) {
            throw new InvalidFacilityDataException("Incorrect data for facility");
        }
        final Facility facility = facilityMapper.facilityDtoToFacility(facilityDto);
        facilityRepository.save(facility);
    }

    private boolean isValid(FacilityDto facilityDto) {
        return facilityDto.getName() != null && facilityDto.getCity() != null && facilityDto.getPostCode() != null
                && facilityDto.getStreet() != null && facilityDto.getNumber() != null;
    }
}
