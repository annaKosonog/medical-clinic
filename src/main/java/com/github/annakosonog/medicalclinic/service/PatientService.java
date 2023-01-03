package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private List<Patient> patients = new ArrayList<>();


    public List<Patient> getPatients() {
        return patients;
    }


    public Patient getPatient(String email) {
        return getPatientByEmail(email).orElseThrow(PatientNotFoundException::new);
    }

    public void addPatient(Patient patient) {
        Optional<Patient> existingPatient = patients.stream()
                .filter(patient1 -> patient1.getEmail().equals(patient.getEmail()))
                .findFirst();
        if (existingPatient.isPresent()) {
            throw new PatientAlreadyExistsException();
        }
        if (patient.getEmail() == null) {
            throw new IllegalArgumentException("Invalid patient data");
        }
        patients.add(patient);
    }

    public void deletePatient(String email){
        final Patient patient = getPatientByEmail(email).orElseThrow(PatientNotFoundException::new);
       patients.remove(patient);
    }

    public void  updatePatient(Patient patient, String email) {
        Patient patientData = getPatientByEmail(email)
                .orElseThrow((PatientNotFoundException::new));

        patientData.setEmail(patient.getEmail());
        patientData.setPassword(patient.getPassword());
        patientData.setIdCardNo(patient.getIdCardNo());
        patientData.setFirstName(patient.getFirstName());
        patientData.setLastName(patient.getLastName());
        patientData.setNumberPhone(patient.getNumberPhone());
        patientData.setBirthday(patient.getBirthday());
        }

    public void updatePasswordPatient(String email, String password){
        Patient patients = getPatientByEmail(email).orElseThrow(PatientNotFoundException::new);
        patients.setPassword(password);
    }

    private Optional<Patient> getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }
}
