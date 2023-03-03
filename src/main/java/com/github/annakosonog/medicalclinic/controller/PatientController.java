package com.github.annakosonog.medicalclinic.controller;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.service.PatientService;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/patients")
@RestController
public class PatientController {

    private final PatientService service;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(service.getPatientsDto());
    }

    @GetMapping("/{email}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String email) {
        return ResponseEntity.ok(service.getPatient(email));
    }

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        service.addPatient(patient);
        return ResponseEntity.ok("Patient was added successfully");
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable String email) {
        service.deletePatient(email);
        return ResponseEntity.ok("Patient was deleted successfully");
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> updatePatient(@PathVariable String email, @RequestBody Patient patient) {
        service.updatePatient(patient, email);
        return ResponseEntity.ok("Patient was update successfully");
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String password) {
        service.updatePasswordPatient(email, password);
        return ResponseEntity.ok("Password changed successfully");
    }
}
