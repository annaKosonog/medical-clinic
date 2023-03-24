package com.github.annakosonog.medicalclinic.controller;

import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import com.github.annakosonog.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/doctors")
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<String> addNewDoctor(@RequestBody Doctor doctor) {
        doctorService.addDoctor(doctor);
        return ResponseEntity.ok("Doctor was added successfully");
    }

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctor());
    }
}
