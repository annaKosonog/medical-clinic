package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.InvalidPatientDataException;
import com.github.annakosonog.medicalclinic.exception.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.PatientException;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.mapper.PatientMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private Patient beforeSaveAKlaraKowalska;
    private Patient savedAKlaraKowalska;
    private Patient aPawelNowak;

    @BeforeEach
    void setupUp() {
        beforeSaveAKlaraKowalska = Patient.builder()
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();

        savedAKlaraKowalska = Patient.builder()
                .id(1L)
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();
        when(patientRepository.save(beforeSaveAKlaraKowalska)).thenReturn(savedAKlaraKowalska);

        aPawelNowak = Patient.builder()
                .id(2L)
                .email("pawel@wp.pl")
                .password("pawel123")
                .idCardNo(19852578L)
                .firstName("Pawel")
                .lastName("Nowak")
                .numberPhone(574522511)
                .birthday(LocalDate.of(2001, 2, 15))
                .build();
    }

    @Test
    void getPatients_DataCorrect_FindAllPatient() {
        final List<Patient> patients = List.of(beforeSaveAKlaraKowalska, aPawelNowak);
        when(patientRepository.findAll()).thenReturn(patients);
        final List<Patient> expected = patientService.getPatients();
        assertEquals(expected, patients);
        verify(patientRepository).findAll();
        assertThat(expected).hasSize(2).contains(beforeSaveAKlaraKowalska, aPawelNowak);
    }

    @Test
    void getPatientsDto_DataCorrect_MapToDto() {
        Patient aPawelNowak = this.aPawelNowak;
        PatientDTO actual = PatientMapper.INSTANCE.patientToPatientDto(aPawelNowak);
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo("pawel@wp.pl");
        assertThat(actual.getFirstName()).isEqualTo("Pawel");
        assertThat(actual.getLastName()).isEqualTo("Nowak");
    }

    @Test
    void getPatient_DataCorrect_FindByEmail() {
        final String email = "klara@wp.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(beforeSaveAKlaraKowalska));
        when(patientMapper.patientToPatientDto(beforeSaveAKlaraKowalska)).thenReturn(aKlaraKowalskaDto());
        final PatientDTO expected = patientService.getPatient(email);
        assertEquals(expected.getEmail(), email);
        assertEquals(expected.getLastName(), "Kowalska");
    }

    @Test
    void getPatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatient(email));
    }

    @Test
    void addPatient_DataCorrect_PatientSaved() {
        when(patientRepository.findByEmail("pawel")).thenReturn(Optional.empty());
        patientService.addPatient(aPawelNowak);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void addPatient_DataIncorrect_ThrowingPatientAlreadyException() {
        when(patientRepository.findByEmail("klara@wp.pl")).thenReturn(Optional.of(beforeSaveAKlaraKowalska));
        assertThrows(PatientAlreadyExistsException.class, () -> patientService.addPatient(beforeSaveAKlaraKowalska));
    }

    @Test
    void addPatient_DataIncorrect_ThrowingPatientException() {
        Patient aJanMnich = Patient.builder()
                .email(null)
                .password("jan123")
                .idCardNo(198578L)
                .firstName("Jan")
                .lastName("Mnich")
                .numberPhone(574522511)
                .birthday(LocalDate.of(2001, 2, 15))
                .build();
        when(patientRepository.findByEmail("email")).thenReturn(Optional.empty());
        assertThrows(PatientException.class, () -> patientService.addPatient(aJanMnich));
    }

    @Test
    void deletePatient_DataCorrect_PatientDeleted() {
        final String email = "klara@wp.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(savedAKlaraKowalska));
        doNothing().when(patientRepository).delete(savedAKlaraKowalska);
        patientService.deletePatient(email);
        verify(patientRepository, times(1)).delete(savedAKlaraKowalska);
    }

    @Test
    void deletePatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        doNothing().when(patientRepository).delete(beforeSaveAKlaraKowalska);
        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(email));
        verify(patientRepository, never()).delete(any(Patient.class));
    }

    @Test
    void updatePatient_DataCorrect_PatientUpdated() {
        final Patient aEditKlaraKowalska = Patient.builder()
                .email("laura@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();

        final String email = "klara@wp.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(savedAKlaraKowalska));
        patientService.updatePatient(aEditKlaraKowalska, email);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_DataIncorrect_ThrowingPatientException() {
        final String email = "klara@wp.pl";
        final Patient aEditKlaraKowalska = Patient.builder()
                .email("laura@wp.pl")
                .idCardNo(12345L)
                .password("laura123")
                .firstName("Laura")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(savedAKlaraKowalska));
        assertThrows(PatientException.class, () -> patientService.updatePatient(aEditKlaraKowalska, email));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void updatePatient_DataIncorrect_ThrowingInvalidPatientDataException() {
        final String email = "klara@wp.pl";
        final Patient aEditKlaraKowalska = Patient.builder()
                .email(null)
                .idCardNo(1234578L)
                .password(null)
                .firstName(null)
                .lastName(null)
                .numberPhone(null)
                .birthday(null)
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(savedAKlaraKowalska));
        assertThrows(InvalidPatientDataException.class, () -> patientService.updatePatient(aEditKlaraKowalska, email));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void updatePatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        final Patient aEditKlaraKowalska = Patient.builder()
                .email("laura@wp.pl")
                .idCardNo(12345L)
                .password("laura123")
                .firstName("Laura")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();

        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(aEditKlaraKowalska, email));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void updatePasswordPatient_DataCorrect_PasswordUpdated() {
        final String email = "klara@wp.pl";
        final String password = "klara124";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(savedAKlaraKowalska));
        patientService.updatePasswordPatient(email, password);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePasswordPatient_DataIncorrect_ThrowingInvalidPatientDataException() {
        final String email = "klara@wp.pl";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.of(savedAKlaraKowalska));
        assertThrows(NullPointerException.class, () -> patientService.updatePasswordPatient(email, null));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void updatePasswordPatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        final String password = "klara124";
        when(patientRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.updatePasswordPatient(email, password));
    }

    private PatientDTO aKlaraKowalskaDto() {
        return PatientDTO.builder()
                .email("klara@wp.pl")
                .firstName("Klara")
                .lastName("Kowalska")
                .build();
    }
}

