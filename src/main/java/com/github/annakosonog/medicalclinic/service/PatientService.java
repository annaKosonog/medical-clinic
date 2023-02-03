package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.InvalidPatientDataException;
import com.github.annakosonog.medicalclinic.exception.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.PatientException;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.mapper.PatientMapperImpl;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepositoryImpl patientRepository;
    private final PatientMapperImpl patientMapper;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public List<PatientDTO> getPatientsDto() {
        return getPatients().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
        return patientMapper.patientToPatientDto(patient);
    }

    public void addPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new PatientAlreadyExistsException();
        }
        if (patient.getEmail() == null) {
            throw new PatientException("Invalid patient data");
        }
        patientRepository.addPatient(patient);
    }

    public void deletePatient(String email) {
        patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        patientRepository.delete(email);
    }

    public void updatePatient(Patient patient, String email) {
        final Patient entity = patientRepository.findByEmail(email)
                .orElseThrow((PatientNotFoundException::new));
        if (!entity.getIdCardNo().equals(patient.getIdCardNo())) {
            throw new PatientException("Do not change card number");
        }
        if (!isValid(patient)) {
            throw new InvalidPatientDataException("Invalid patient data");
        }
        patientRepository.update(email, patient);
    }

    public void updatePasswordPatient(String email, String password) {
        patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        if (password == null) {
            throw new NullPointerException("Password not be null");
        }
        patientRepository.updatePassword(email, password);
    }

    private boolean isValid(Patient patient) {
        return patient.getIdCardNo() != null && patient.getEmail() != null && patient.getFirstName() != null && patient.getLastName() != null
                && patient.getNumberPhone() != null && patient.getPassword() != null && patient.getBirthday() != null;
    }
}
