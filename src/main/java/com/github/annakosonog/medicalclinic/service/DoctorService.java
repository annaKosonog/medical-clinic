package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.DataAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.InvalidDetailsException;
import com.github.annakosonog.medicalclinic.mapper.DoctorMapper;
import com.github.annakosonog.medicalclinic.model.AssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import com.github.annakosonog.medicalclinic.model.Facility;
import com.github.annakosonog.medicalclinic.repository.DoctorRepository;
import com.github.annakosonog.medicalclinic.repository.FacilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DoctorService {

    private DoctorRepository doctorRepository;
    private DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;
    private final FacilityRepository facilityRepository;

    @Transactional
    public void addDoctor(AssignDoctorDto dto) {
        final Optional<Doctor> existsDoctor = doctorRepository.findByEmail(dto.getEmail());

        if (existsDoctor.isPresent()) {
            throw new DataAlreadyExistsException("Doctor already exists");
        }

        if (!isValid(dto)) {
            throw new InvalidDetailsException("Invalid doctor data");
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        final Doctor doctor = doctorMapper.assignDoctorDtoToDoctor(dto);
        doctorRepository.save(doctor);
    }

    @Transactional
    public String assignDoctorToFacility(Long doctorId, Long facilityId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(IllegalArgumentException::new);
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(IllegalArgumentException::new);
        doctor.getFacilities().add(facility);
        doctorRepository.save(doctor);
        return "ok";
    }

    public List<DoctorDto> getAllDoctor() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> DoctorDto.builder()
                        .email(doctor.getEmail())
                        .firstName(doctor.getFirstName())
                        .lastName(doctor.getLastName())
                        .specialization(doctor.getSpecialization())
                        .facilitiesId(doctor.getFacilities().stream().map(Facility::getId).collect(Collectors.toSet()))
                        .build())
                .collect(Collectors.toList());
    }

    private boolean isValid(AssignDoctorDto dto) {
        return dto.getEmail() != null && dto.getFirstName() != null && dto.getLastName() != null
                && dto.getLastName() != null && dto.getSpecialization().toString() != null
                && dto.getPassword() != null;
    }
}
