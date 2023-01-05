package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.repository.PatientRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepositoryImpl patientRepository;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatient(String email) {
        return patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
    }

    public void addPatient(Patient patient) {
        patientRepository.findByEmail(patient.getEmail()).orElseThrow(PatientAlreadyExistsException::new);
        if (patient.getEmail() == null) {
            throw new IllegalArgumentException("Invalid patient data");
        }
    }

    public void deletePatient(String email) {
        patientRepository.delete(email);
    }

    public void updatePatient(Patient patient, String email) {
        patientRepository.findByEmail(email)
                .orElseThrow((PatientNotFoundException::new));
        patientRepository.update(email, patient);
    }

    public void updatePasswordPatient(String email, String password) {
        patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        patientRepository.updatePassword(email, password);
    }
}
