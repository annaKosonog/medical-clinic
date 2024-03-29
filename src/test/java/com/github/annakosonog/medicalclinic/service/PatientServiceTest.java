package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.repository.PatientRepositoryImpl;
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
    private PatientRepositoryImpl patientRepositoryImp;

    @InjectMocks
    private PatientService patientService;

    private Patient aKlaraKowalska;

    private Patient aPawelNowak;

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
        doNothing().when(patientRepositoryImp).addPatient(aKlaraKowalska);

        aPawelNowak = Patient.builder()
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

        final List<Patient> patients = List.of(aKlaraKowalska, aPawelNowak);
        when(patientRepositoryImp.findAll()).thenReturn(patients);
        final List<Patient> expected = patientService.getPatients();
        assertEquals(expected, patients);
        verify(patientRepositoryImp).findAll();
        assertThat(expected).hasSize(2).contains(aKlaraKowalska, aPawelNowak);
    }


    @Test
    void addPatient_DataCorrect_PatientSaved() {
        when(patientRepositoryImp.findByEmail("pawel@wp.pl")).thenReturn(Optional.empty());
        doNothing().when(patientRepositoryImp).addPatient(aPawelNowak);
        patientService.addPatient(aPawelNowak);
        verify(patientRepositoryImp, times(1)).addPatient(any(Patient.class));
    }

    @Test
    void addPatient_DataIncorrect_ThrowingPatientAlreadyException() {
        when(patientRepositoryImp.findByEmail("klara@wp.pl")).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImp).addPatient(aKlaraKowalska);
        assertThrows(PatientAlreadyExistsException.class, () -> patientService.addPatient(aKlaraKowalska));
        verify(patientRepositoryImp, never()).addPatient(any(Patient.class));
    }

    @Test
    void addPatient_DataIncorrect_ThrowingIllegalArgumentException() {
        Patient aJanMnich = Patient.builder()
                .email(null)
                .password("jan123")
                .idCardNo(198578L)
                .firstName("Jan")
                .lastName("Mnich")
                .numberPhone(574522511)
                .birthday(LocalDate.of(2001, 2, 15))
                .build();

        when(patientRepositoryImp.findByEmail("email")).thenReturn(Optional.empty());
        doNothing().when(patientRepositoryImp).addPatient(aJanMnich);
        assertThrows(IllegalArgumentException.class, () -> patientService.addPatient(aJanMnich));
        verify(patientRepositoryImp, never()).addPatient(any(Patient.class));
    }

    @Test
    void deletePatient_DataCorrect_PatientDeleted() {
        final String email = "klara@wp.pl";
        when(patientRepositoryImp.findByEmail(email)).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImp).delete(email);
        patientService.deletePatient(email);
        verify(patientRepositoryImp, times(1)).delete(email);
    }

    @Test
    void deletePatient_DataIncorrect_ThrowingPatientNotFoundException() {
        final String email = "email";
        when(patientRepositoryImp.findByEmail(email)).thenReturn(Optional.empty());
        doNothing().when(patientRepositoryImp).delete(email);
        verify(patientRepositoryImp, never()).delete(email);
        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(email));
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
        when(patientRepositoryImp.findByEmail(email)).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImp).update(email, aEditKlaraKowalska);
        patientService.updatePatient(aEditKlaraKowalska, email);
        verify(patientRepositoryImp, times(1)).update(email, aEditKlaraKowalska);

    }

    @Test
    void updatePasswordPatient_DataCorrect_PasswordUpdated() {
        final String email = "klara@wp.pl";
        final String password = "klara124";
        when(patientRepositoryImp.findByEmail(email)).thenReturn(Optional.of(aKlaraKowalska));
        doNothing().when(patientRepositoryImp).updatePassword(email, password);
        patientService.updatePasswordPatient(email, password);
        verify(patientRepositoryImp, times(1)).updatePassword(email, password);
    }
}
