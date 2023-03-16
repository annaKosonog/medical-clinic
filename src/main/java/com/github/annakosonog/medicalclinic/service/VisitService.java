package com.github.annakosonog.medicalclinic.service;
import com.github.annakosonog.medicalclinic.exception.patient.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.exception.visit.IncorrectVisitException;
import com.github.annakosonog.medicalclinic.exception.visit.PatientVisitIsUnavaible;
import com.github.annakosonog.medicalclinic.exception.visit.VisitNotFoundException;
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
            throw new IncorrectVisitException("Incorrect date");
        }
        if (validTime(dateTime)) {
            throw new IncorrectVisitException("Incorrect time");
        }
        if (visitRepository.existsByTerm(dateTime)) {
            throw new IncorrectVisitException("Date already exists");
        }
        Visit visit = new Visit();
        visit.setTerm(dateTime);
        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }

    public void patientRegistrationForAnAppointment(Long id, PatientDTO patientDTO) {
        Visit visit = visitRepository.findById(id).orElseThrow(VisitNotFoundException::new);

        if (visit.getPatient() != null) {
            throw new PatientVisitIsUnavaible("Date already taken");
        }

        if (visit.getTerm().isBefore(LocalDateTime.now())) {
            throw new IncorrectVisitException("Date is before");
        }
        final Patient patient = patientRepository.findByEmail(patientDTO.getEmail()).orElseThrow(PatientNotFoundException::new);
        visit.setPatient(patient);
        visitRepository.save(visit);
    }

    public List<VisitDTO> findAllVisitPatient(PatientDTO patientDTO) {
        Patient patient = patientRepository.findByEmail(patientDTO.getEmail()).orElseThrow(PatientNotFoundException::new);
        return visitRepository.findAll()
                .stream()
                .filter(visit -> visit.getPatient().getId().equals(patient.getId()))
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }

    private boolean validTime(LocalDateTime dateTime) {
        return dateTime.getMinute() % 15 != 0;
    }
}
