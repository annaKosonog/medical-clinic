package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.model.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/patients")
@RestController
public class PatientController {

    private List<Patient> patients = new ArrayList<>();


    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Patient> getPatient(@PathVariable String email) {
        return getPatientByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(PatientNotFoundException::new);
    }


    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        patients.add(patient);
        return ResponseEntity.ok("Patient was added successfully");
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable("email") String email) {
        getPatientByEmail(email)
                .map(deletePatients -> ResponseEntity.ok(patients.remove(deletePatients)))
                .orElseThrow(PatientNotFoundException::new);

        return ResponseEntity.ok("Patient was deleted successfully");
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> updatePatient(@PathVariable("email") String email, @RequestBody Patient patient) {
        final Optional<Patient> patientDate = getPatientByEmail(email);
        if (patientDate.isPresent()) {
            Patient updatePatient = patientDate.get();
            updatePatient.setEmail(patient.getEmail());
            updatePatient.setPassword(patient.getPassword());
            updatePatient.setIdCardNo(patient.getIdCardNo());
            updatePatient.setFirstName(patient.getFirstName());
            updatePatient.setLastName(patient.getLastName());
            updatePatient.setNumberPhone(patient.getNumberPhone());
            updatePatient.setBirthday(patient.getBirthday());
            patients.add(updatePatient);
            return ResponseEntity.ok("Patient was update successfully");
        } else {
            return ResponseEntity.ok("Patient  update failed");
        }
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String password) {
        Patient patient = getPatientByEmail(email).orElseThrow(PatientNotFoundException::new);
        patient.setPassword(password);
        return ResponseEntity.ok("Password changed successfully");
    }


    private Optional<Patient> getPatientByEmail(String email) {
        return patients.stream()
                .filter(patient -> patient.getEmail().equals(email))
                .findFirst();
    }
}
