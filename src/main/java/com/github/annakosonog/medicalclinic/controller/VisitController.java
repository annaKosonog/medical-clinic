package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.VisitDTO;
import com.github.annakosonog.medicalclinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public ResponseEntity<VisitDTO> addNewVisit(@RequestBody LocalDateTime dateTime) {
        VisitDTO newVisit = visitService.createNewVisit(dateTime);
        return ResponseEntity.ok(newVisit);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> appointmentOfThePatient(@PathVariable Long id, @RequestBody String email) {
        visitService.patientRegistrationForAnAppointment(id, email);
        return ResponseEntity.ok("The visit has been arranged");
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<VisitDTO>> allPatientVisits(@PathVariable String email) {
        return ResponseEntity.ok(visitService.findAllVisitPatient(email));
    }
}
