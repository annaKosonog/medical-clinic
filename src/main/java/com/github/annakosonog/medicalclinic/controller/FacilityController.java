package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.FacilityDto;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import com.github.annakosonog.medicalclinic.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/facilities")
@RestController
public class FacilityController {

    private final FacilityService facilityService;

    @Operation(summary = "Creating a new facility ", tags = "FACILITY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Adding a facility to our system",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = FacilityDto.class))}),
            @ApiResponse(responseCode = "400", description = "Facility not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @PostMapping
    ResponseEntity<String> addNewFacility(@RequestBody FacilityDto facilityDto) {
        facilityService.createANewFacility(facilityDto);
        return ResponseEntity.ok("Facility is added successfully");
    }

    @Operation(summary = "Get all facility ", tags = "FACILITY")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show all saved facilities",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VisitDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Facility not found",
                    content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = String.class))})
    })
    @GetMapping
    ResponseEntity<List<FacilityDto>> getAllFacility() {
        return ResponseEntity.ok(facilityService.findAllFacility());
    }
}
