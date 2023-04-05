package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.AssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import com.github.annakosonog.medicalclinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/doctors")
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Add new doctor", tags = "DOCTOR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor was added successfully",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AssignDoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PostMapping
    public ResponseEntity<String> addNewDoctor(@RequestBody AssignDoctorDto assignDoctorDto) {
        doctorService.addDoctor(assignDoctorDto);
        return ResponseEntity.ok("Doctor was added successfully");
    }

    @Operation(summary = "Find all doctor", tags = "DOCTOR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Find all doctor",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DoctorDto.class))}),
            @ApiResponse(responseCode = "400", description = "Patient not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctor());
    }

    @Operation(summary = "Assigning a doctor to the facility", tags = "DOCTOR")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Long.class))}),
            @ApiResponse(responseCode = "400", description = "Doctor or facility not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> assignDoctorToFacility(@PathVariable Long id, @RequestParam Long facilityId) {
        return ResponseEntity.ok(doctorService.assignDoctorToFacility(id, facilityId));
    }
}
