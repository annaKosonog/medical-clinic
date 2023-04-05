package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.DataAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.DataNotFoundException;
import com.github.annakosonog.medicalclinic.exception.InvalidDetailsException;
import com.github.annakosonog.medicalclinic.exception.MedicalClinicException;
import com.github.annakosonog.medicalclinic.mapper.PatientMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public List<PatientDTO> getPatientsDto() {
        return getPatients().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("Patient not found"));
        return patientMapper.patientToPatientDto(patient);
    }

    public void addPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new DataAlreadyExistsException("Patient already exists");
        }
        if (patient.getEmail() == null) {
            throw new InvalidDetailsException("Invalid patient data");
        }
        if (!(isValid(patient))) {
            throw new InvalidDetailsException("Invalid patient data");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
    }

    public void deletePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        patientRepository.delete(patient);
    }

    public void updatePatient(Patient patient, String email) {
        Patient entity = patientRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));

        if (!entity.getIdCardNo().equals(patient.getIdCardNo())) {
            throw new MedicalClinicException("Do not change card number");
        }
        if (patient.getId() == null) {
            patient.setId(entity.getId());
        }
        if (!isValid(patient)) {
            throw new InvalidDetailsException("Invalid patient data");
        }
        patientRepository.save(patient);
    }

    public void updatePasswordPatient(String email, String password) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("Patient not found"));
        if (password == null) {
            throw new NullPointerException("Password not be null");
        }
        patient.setPassword(password);
        patientRepository.save(patient);
    }

    private boolean isValid(Patient patient) {
        return patient.getIdCardNo() != null && patient.getEmail() != null && patient.getFirstName() != null && patient.getLastName() != null
                && patient.getNumberPhone() != null && patient.getPassword() != null && patient.getBirthday() != null;
    }
}
