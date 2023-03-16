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

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getPatientsDto());
    }

    @GetMapping("/{email}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String email) {
        return ResponseEntity.ok(patientService.getPatient(email));
    }

    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return ResponseEntity.ok("Patient was added successfully");
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
        return ResponseEntity.ok("Patient was deleted successfully");
    }

    @PutMapping("/{email}")
    public ResponseEntity<String> updatePatient(@PathVariable String email, @RequestBody Patient patient) {
        patientService.updatePatient(patient, email);
        return ResponseEntity.ok("Patient was update successfully");
    }

    @PatchMapping("/{email}")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String password) {
        patientService.updatePasswordPatient(email, password);
        return ResponseEntity.ok("Password changed successfully");
    }
}
