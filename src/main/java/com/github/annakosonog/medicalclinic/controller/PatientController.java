package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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


    @Operation(summary = "Find all patient", tags = "PATIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all patient",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getPatientsDto());
    }

    @Operation(summary = "Get patient by email", tags = "PATIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient exists in our system",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/{email}")
    public ResponseEntity<PatientDTO> getPatient(@PathVariable String email) {
        return ResponseEntity.ok(patientService.getPatient(email));
    }

    @Operation(summary = "Add new  patient", tags = "PATIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient was added successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PostMapping
    public ResponseEntity<String> addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return ResponseEntity.ok("Patient was added successfully");
    }

    @Operation(summary = "Delete patient", tags = "PATIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient was deleted successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
        return ResponseEntity.ok("Patient was deleted successfully");
    }

    @Operation(summary = "Update patient", tags = "PATIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient was update successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PutMapping("/{email}")
    public ResponseEntity<String> updatePatient(@PathVariable String email, @RequestBody Patient patient) {
        patientService.updatePatient(patient, email);
        return ResponseEntity.ok("Patient was update successfully");
    }

    @Operation(summary = "Patient password update", tags = "PATIENT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PatchMapping("/{email}")
    public ResponseEntity<String> editPatientPassword(@PathVariable String email, @RequestBody String password) {
        patientService.updatePasswordPatient(email, password);
        return ResponseEntity.ok("Password changed successfully");
    }
}
