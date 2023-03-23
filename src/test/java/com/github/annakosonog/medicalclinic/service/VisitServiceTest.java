package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.patient.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.exception.visit.IncorrectVisitException;
import com.github.annakosonog.medicalclinic.exception.visit.PatientVisitIsUnavailable;
import com.github.annakosonog.medicalclinic.exception.visit.VisitNotFoundException;
import com.github.annakosonog.medicalclinic.mapper.VisitMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.Visit;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import com.github.annakosonog.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VisitServiceTest {

    @Mock
    PatientRepository patientRepository;

    @Mock
    VisitRepository visitRepository;

    @Mock
    VisitMapper visitMapper;

    @InjectMocks
    VisitService visitService;

    private Patient beforeRegistrationPatient;
    private Patient afterRegistrationPatientToDb;
    private Visit beforeSaveVisit;
    private Visit savedVisitToDb;
    private Visit savedVisitWithPatient;

    private LocalDateTime dateTime = LocalDateTime.now().plusDays(2L).withMinute(15).withSecond(0).withNano(0);

    @BeforeEach
    void setupUp() {
        beforeSaveVisit = Visit.builder()
                .term(dateTime)
                .id(null)
                .patient(null)
                .build();

        savedVisitToDb = Visit.builder()
                .id(1L)
                .term(dateTime)
                .patient(null)
                .build();
        when(visitRepository.save(beforeSaveVisit)).thenReturn(savedVisitToDb);

        savedVisitWithPatient = Visit.builder()
                .term(dateTime)
                .patient(buildPatient())
                .id(1L)
                .build();

        beforeRegistrationPatient = Patient.builder()
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();

        afterRegistrationPatientToDb = Patient.builder()
                .id(1L)
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();
        when(patientRepository.save(beforeRegistrationPatient)).thenReturn(afterRegistrationPatientToDb);
    }

    @Test
    void createNewVisit_DataCorrect_CreateDate() {
        LocalDateTime dateTime = LocalDateTime.of(2222, 3, 16, 16, 15);
        visitService.createNewVisit(dateTime);
        verify(visitRepository, times(1)).save(any(Visit.class));
        assertThat(visitRepository.existsByTerm(dateTime));
    }

    @Test
    void createNewVisit_DataIncorrect_IncorrectDate() {
        LocalDateTime beforeDate = LocalDateTime.of(2022, 3, 17, 16, 15);
        assertThrows(IncorrectVisitException.class, () -> visitService.createNewVisit(beforeDate));
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    void createNewVisit_DataIncorrect_IncorrectTime() {
        final LocalDateTime dateTime = LocalDateTime.now().withMinute(17);
        assertThrows(IncorrectVisitException.class, () -> visitService.createNewVisit(dateTime));
    }

    @Test
    void createNewVisit_DataIncorrect_DateAlreadyExists() {
        when(visitRepository.existsByTerm(dateTime)).thenReturn(true);
        assertThrows(IncorrectVisitException.class, () -> visitService.createNewVisit(dateTime));
    }

    @Test
    void patientRegistrationForAnAppointment_DataCorrect() {
        final long id = 1L;
        final String email = "klara@wp.pl";
        when(visitRepository.findById(id)).thenReturn(Optional.of(savedVisitToDb));
        when(patientRepository.findByEmail("klara@wp.pl")).thenReturn(Optional.of(afterRegistrationPatientToDb));
        visitService.patientRegistrationForAnAppointment(id, email);
        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    @Test
    void patientRegistrationForAnAppointment_VisitNotFoundException() {
        final long id = 25L;
        final String email = "klara@wp.pl";
        when(visitRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(VisitNotFoundException.class, () -> visitService.patientRegistrationForAnAppointment(id, email));
    }

    @Test
    void patientRegistrationForAnAppointment_PatientVisitIsUnavailable() {
        final long id = 1L;
        final String email = "klara@wp.pl";
        Visit incorrectData = this.savedVisitToDb;
        incorrectData.setPatient(afterRegistrationPatientToDb);
        when(visitRepository.findById(id)).thenReturn(Optional.of(incorrectData));
        assertThrows(PatientVisitIsUnavailable.class, () -> visitService.patientRegistrationForAnAppointment(id, email));
    }

    @Test
    void patientRegistrationForAnAppointment_IncorrectVisitException() {
        final long id = 1L;
        final String email = "klara@wp.pl";
        Visit incorrectData = savedVisitToDb;
        incorrectData.setTerm(LocalDateTime.now().minusDays(1L).withMinute(15));
        when(visitRepository.findById(id)).thenReturn(Optional.of(incorrectData));
        assertThrows(IncorrectVisitException.class, () -> visitService.patientRegistrationForAnAppointment(id, email));
    }

    @Test
    void patientRegistrationForAnAppointment_PatientNotFoundException() {
        final long id = 1L;
        final String email = "klara.pl";

        when(visitRepository.findById(id)).thenReturn(Optional.of(savedVisitToDb));
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        verify(visitRepository, never()).save(any(Visit.class));
        assertThrows(PatientNotFoundException.class, () -> visitService.patientRegistrationForAnAppointment(id, email));
    }

    @Test
    void createNewVisit_DataCorrect_MapToDto() {
        Visit visit = savedVisitToDb;
        VisitDTO mapToVisitDto = visitMapper.INSTANCE.visitToVisitDto(visit);
        assertThat(mapToVisitDto).isNotNull();
        assertThat(mapToVisitDto.getTerm()).isEqualTo(visit.getTerm());
    }

    @Test
    void findAllVisitPatient_DataCorrect() {
        final String email = "klara@wp.pl";
        Patient patient = buildPatient();
        patient.setVisits(Collections.singletonList(savedVisitWithPatient));

        when(patientRepository.save(beforeRegistrationPatient)).thenReturn(patient);
        when(visitRepository.save(beforeSaveVisit)).thenReturn(savedVisitWithPatient);
        when(visitMapper.visitToVisitDto(savedVisitWithPatient)).thenReturn(buildVisitDto());
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(patient));

        final List<VisitDTO> actual = visitService.findAllVisitPatient(email);
        assertThat(actual.get(0).getPatientId()).isEqualTo(1);
        assertThat(actual.get(0).getTerm()).isEqualTo(dateTime);
    }

    @Test
    void findAllVisitPatient_PatientNotFoundException() {
        final String email = "test";
        Patient patient = buildPatient();
        patient.setVisits(Collections.singletonList(savedVisitWithPatient));

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> visitService.findAllVisitPatient(email));
    }


    private Patient buildPatient() {
        return Patient.builder()
                .id(1L)
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .visits(Collections.singletonList(savedVisitWithPatient))
                .build();
    }

    private VisitDTO buildVisitDto() {
        return VisitDTO.builder()
                .term(dateTime)
                .patientId(1L)
                .build();
    }
}
