package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.doctor.DoctorAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.doctor.InvalidDoctorException;
import com.github.annakosonog.medicalclinic.mapper.DoctorMapper;
import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import com.github.annakosonog.medicalclinic.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DoctorService {

    private DoctorRepository doctorRepository;
    private DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;

    public void addDoctor(Doctor doctor) {
        final Optional<Doctor> existsDoctor = doctorRepository.findByEmail(doctor.getEmail());

        if (existsDoctor.isPresent()) {
            throw new DoctorAlreadyExistsException();
        }

        if (!isValid(doctor)) {
            throw new InvalidDoctorException("Invalid doctor data");
        }
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);
    }

    public List<DoctorDto> getAllDoctor() {
        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::doctorToDoctorDto)
                .collect(Collectors.toList());
    }

    private boolean isValid(Doctor doctor) {
        return doctor.getEmail() != null && doctor.getFirstName() != null && doctor.getLastName() != null
                && doctor.getLastName() != null && doctor.getSpecialization().toString() != null
                && doctor.getPassword() != null;
    }
}
