package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.repository.PatientRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PatientServiceTest {

    @Mock
    private PatientRepositoryImpl patientRepositoryImpl;

    @InjectMocks
    private PatientService patientService;

    private Patient aKlaraKowalska;
    private Patient aPawelNowak;
    private Patient aEditKlaraKowalska;

    @BeforeEach
    void setupUp() {
        aKlaraKowalska = Patient.builder()
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();

        aPawelNowak = Patient.builder()
                .email("pawel@wp.pl")
                .password("pawel123")
                .idCardNo(19852578L)
                .firstName("Pawel")
                .lastName("Nowak")
                .numberPhone(574522511)
                .birthday(LocalDate.of(2001, 2, 15))
                .build();

        aEditKlaraKowalska = Patient.builder()
                .email("laura@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();
    }

    @Test
    void getPatients_DataCorrect_FindAllPatient() {
        final List<Patient> patients = List.of(aKlaraKowalska, aPawelNowak);
        when(patientRepositoryImpl.findAll()).thenReturn(patients);
        final List<Patient> expected = patientService.getPatients();
        assertThat(expected).hasSize(2).contains(aKlaraKowalska, aPawelNowak);
        verify(patientRepositoryImpl).findAll();
    }

    @Test
    void addPatient_DataCorrect_PatientSaved() {
        when(patientRepositoryImpl.findByEmail("pawel@wp.pl")).thenReturn(Optional.empty());
        doNothing().when(patientRepositoryImpl).addPatient(aPawelNowak);
        patientService.addPatient(aPawelNowak);
        verify(patientRepositoryImpl, times(1)).addPatient(aPawelNowak);
    }

    @Test
    void addPatient_DataIncorrect_ThrowingPatientAlreadyException() {
        when(patientRepositoryImpl.findByEmail("klara@wp.pl")).thenReturn(Optional.of(aKlaraKowalska));
        assertThrows(PatientAlreadyExistsException.class, () -> patientService.addPatient(aKlaraKowalska));
        verify(patientRepositoryImpl, never()).addPatient(any(Patient.class));
    }

    @Test
    void addPatient_DataIncorrect_ThrowingIllegalArgumentException() {
        aKlaraKowalska.setEmail(null);
        when(patientRepositoryImpl.findByEmail("email")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> patientService.addPatient(aKlaraKowalska));
        verify(patientRepositoryImpl, never()).addPatient(any(Patient.class));
    }

    @Test
    void deletePatient_DataCorrect_PatientDeleted() {
        final String email = "klara@wp.pl";
        when(patientRepositoryImpl.findByEmail(email)).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImpl).delete(email);
        patientService.deletePatient(email);
        verify(patientRepositoryImpl, times(1)).delete(email);
    }

    @Test
    void deletePatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        when(patientRepositoryImpl.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(email));
        verify(patientRepositoryImpl, never()).delete(email);
    }

    @Test
    void updatePatient_DataCorrect_PatientUpdated() {
        final String email = "klara@wp.pl";
        when(patientRepositoryImpl.findByEmail(email)).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImpl).update(email, aEditKlaraKowalska);
        patientService.updatePatient(aEditKlaraKowalska, email);
        verify(patientRepositoryImpl, times(1)).update(email, aEditKlaraKowalska);
    }

    @Test
    void updatePatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        when(patientRepositoryImpl.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(aEditKlaraKowalska, email));
        verify(patientRepositoryImpl, never()).update(email, aEditKlaraKowalska);
    }

    @Test
    void updatePasswordPatient_DataCorrect_PasswordUpdated() {
        final String email = "klara@wp.pl";
        final String password = "klara124";
        when(patientRepositoryImpl.findByEmail(email)).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImpl).updatePassword(email, password);
        patientService.updatePasswordPatient(email, password);
        verify(patientRepositoryImpl, times(1)).updatePassword(email, password);
    }

    @Test
    void updatePasswordPatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        final String password = "klara124";
        when(patientRepositoryImpl.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(PatientNotFoundException.class, () -> patientService.updatePasswordPatient(password, email));
        verify(patientRepositoryImpl, never()).updatePassword(email, password);
    }
}
