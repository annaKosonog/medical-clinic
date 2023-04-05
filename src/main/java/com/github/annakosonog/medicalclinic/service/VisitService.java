package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.DataAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.DataNotFoundException;
import com.github.annakosonog.medicalclinic.exception.InvalidDetailsException;
import com.github.annakosonog.medicalclinic.mapper.VisitMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.model.Visit;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import com.github.annakosonog.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VisitService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    public VisitDTO createNewVisit(LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidDetailsException("Incorrect date");
        }
        if (validTime(dateTime)) {
            throw new InvalidDetailsException("Incorrect time");
        }
        if (visitRepository.existsByTerm(dateTime)) {
            throw new DataAlreadyExistsException("Date already exists");
        }
        Visit visit = new Visit();
        visit.setTerm(dateTime);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    public void patientRegistrationForAnAppointment(Long id, PatientDTO patientDTO) {
        Visit visit = visitRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Visit not found"));

        if (visit.getPatient() != null) {
            throw new DataAlreadyExistsException("Date already taken");
        }

        if (visit.getTerm().isBefore(LocalDateTime.now())) {
            throw new InvalidDetailsException("Date is before");
        }
        final Patient patient = patientRepository.findByEmail(patientDTO.getEmail()).orElseThrow(() -> new DataNotFoundException("Patient not found"));
        visit.setPatient(patient);
        visitRepository.save(visit);
    }

    public List<VisitDTO> findAllVisitPatient(PatientDTO patientDTO) {
        Patient patient = patientRepository.findByEmail(patientDTO.getEmail()).orElseThrow(() -> new DataNotFoundException("Patient not found"));
        return visitRepository.findAll()
                .stream()
                .filter(visit -> visit.getPatient().getId().equals(patient.getId()))
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }

    private boolean validTime(LocalDateTime dateTime) {
        return dateTime.getMinute() % 15 != 0;
    }

    public void deleteAll() {
        visitRepository.deleteAll();
    }
}
