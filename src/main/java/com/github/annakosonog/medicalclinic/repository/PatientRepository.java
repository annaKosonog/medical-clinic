package com.github.annakosonog.medicalclinic.repository;

import com.github.annakosonog.medicalclinic.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientRepository {

    List<Patient> findAll();

    Optional<Patient> findByEmail(String email);

    void addPatient(Patient patient);

    void delete(String email);

    void update(String email, Patient patient);

    void updatePassword(String email, String password);
}
