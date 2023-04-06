package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.AssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import com.github.annakosonog.medicalclinic.service.VisitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/visits")
@RestController
public class VisitController {

    private final VisitService visitService;

    @Operation(summary = "Creating a new visit ", tags = "VISIT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit was added successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AssignDoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PostMapping
    public ResponseEntity<String> addNewVisit(@RequestBody LocalDateTime dateTime) {
        visitService.createNewVisit(dateTime);
        return ResponseEntity.ok("Visit  was added successfully");
    }

    @Operation(summary = "Making an appointment for a patient ", tags = "VISIT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The visit has been arranged",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PatientDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Visit not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> appointmentOfThePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        visitService.patientRegistrationForAnAppointment(id, patientDTO);
        return ResponseEntity.ok("The visit has been arranged");
    }

    @Operation(summary = "Get visit by email", tags = "VISIT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visit exists in our system",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VisitDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/{email}")
    public ResponseEntity<List<VisitDTO>> allPatientVisits(@PathVariable PatientDTO patientDTO) {
        return ResponseEntity.ok(visitService.findAllVisitPatient(patientDTO));
    }

    public void removeVisit() {
        visitService.deleteAll();
    }
}
