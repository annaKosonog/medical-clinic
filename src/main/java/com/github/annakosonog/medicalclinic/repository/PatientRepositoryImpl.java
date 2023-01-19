package com.github.annakosonog.medicalclinic.repository;

import com.github.annakosonog.medicalclinic.model.Patient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PatientRepositoryImpl implements PatientRepository {

    private List<Patient> patients = new ArrayList<>();

    @Override
    public List<Patient> findAll() {
        return patients;
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    @Override
    public void delete(String email) {
        patients.removeIf(patient -> patient.getEmail().equals(email));
    }

    @Override
    public void update(String email, Patient patient) {
        findByEmail(email).ifPresent(patientData -> {
            patientData.setEmail(patient.getEmail());
            patientData.setPassword(patient.getPassword());
            patientData.setIdCardNo(patient.getIdCardNo());
            patientData.setFirstName(patient.getFirstName());
            patientData.setLastName(patient.getLastName());
            patientData.setNumberPhone(patient.getNumberPhone());
            patientData.setBirthday(patient.getBirthday());
        });
    }

    @Override
    public void updatePassword(String email, String password) {
        findByEmail(email).ifPresent(patient -> patient.setPassword(password));
    }
}
