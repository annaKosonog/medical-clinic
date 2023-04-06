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

    private LocalDateTime dateTime = LocalDateTime.now().plusDays(2L).withMinute(15).withSecond(0).withNano(0);

    @BeforeEach
    void setupUp() {
        beforeSaveVisit = Visit.builder()
                .term(dateTime)
                .id(null)
                .patient(null)
                .build();

        savedVisitToDb = Visit.builder()
                .term(dateTime)
                .id(1L)
                .patient(null)
                .build();
        when(visitRepository.save(beforeSaveVisit)).thenReturn(savedVisitToDb);

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
        assertThrows(InvalidDetailsException.class, () -> visitService.createNewVisit(beforeDate));
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    void createNewVisit_DataIncorrect_IncorrectTime() {
        final LocalDateTime dateTime = LocalDateTime.now().withMinute(17);
        assertThrows(InvalidDetailsException.class, () -> visitService.createNewVisit(dateTime));
    }

    @Test
    void createNewVisit_DataIncorrect_DateAlreadyExists() {
        when(visitRepository.existsByTerm(dateTime)).thenReturn(true);
        assertThrows(DataAlreadyExistsException.class, () -> visitService.createNewVisit(dateTime));
    }

    @Test
    void patientRegistrationForAnAppointment_DataCorrect() {
        final long id = 1L;
        when(visitRepository.findById(id)).thenReturn(Optional.of(savedVisitToDb));
        when(patientRepository.findByEmail("klara@wp.pl")).thenReturn(Optional.of(afterRegistrationPatientToDb));
        visitService.patientRegistrationForAnAppointment(id, buildPatientDto());
        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    @Test
    void patientRegistrationForAnAppointment_VisitNotFoundException() {
        final long id = 25L;
        when(visitRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> visitService.patientRegistrationForAnAppointment(id, buildPatientDto()));
    }

    @Test
    void patientRegistrationForAnAppointment_DataAlreadyExistsException() {
        final long id = 1L;
        Visit incorrectData = this.savedVisitToDb;
        incorrectData.setPatient(afterRegistrationPatientToDb);
        when(visitRepository.findById(id)).thenReturn(Optional.of(incorrectData));
        assertThrows(DataAlreadyExistsException.class, () -> visitService.patientRegistrationForAnAppointment(id, buildPatientDto()));
    }

    @Test
    void patientRegistrationForAnAppointment_IncorrectVisitException() {
        final long id = 1L;
        Visit incorrectData = savedVisitToDb;
        incorrectData.setTerm(LocalDateTime.now().minusDays(1L).withMinute(15));
        when(visitRepository.findById(id)).thenReturn(Optional.of(incorrectData));
        assertThrows(InvalidDetailsException.class, () -> visitService.patientRegistrationForAnAppointment(id, buildPatientDto()));
    }

    @Test
    void createNewVisit_DataCorrect_MapToDto() {
        Visit visit = savedVisitToDb;
        VisitDTO mapToVisitDto = visitMapper.INSTANCE.visitToVisitDto(visit);
        assertThat(mapToVisitDto).isNotNull();
        assertThat(mapToVisitDto.getTerm()).isEqualTo(visit.getTerm());
    }

    private PatientDTO buildPatientDto() {
        return PatientDTO.builder()
                .email("klara@wp.pl")
                .firstName("Klara")
                .lastName("Kowalska")
                .build();
    }
}
