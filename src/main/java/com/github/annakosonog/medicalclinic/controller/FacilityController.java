package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.Facility;
import com.github.annakosonog.medicalclinic.model.FacilityDto;
import com.github.annakosonog.medicalclinic.service.FacilityService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    ResponseEntity<String> addNewFacility(@RequestBody FacilityDto facilityDto) {
        facilityService.createANewFacility(facilityDto);
        return ResponseEntity.ok("Facility is added successfully");
    }

    @GetMapping
    ResponseEntity<List<FacilityDto>> getAllFacility() {
        return ResponseEntity.ok(facilityService.findAllFacility());
    }
}
