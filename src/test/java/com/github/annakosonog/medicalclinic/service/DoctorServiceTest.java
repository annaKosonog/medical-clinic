package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.doctor.DoctorAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.doctor.InvalidDoctorException;
import com.github.annakosonog.medicalclinic.mapper.DoctorMapper;
import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import com.github.annakosonog.medicalclinic.model.Specialization;
import com.github.annakosonog.medicalclinic.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DoctorServiceTest {

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    DoctorMapper doctorMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    DoctorService doctorService;

    private Doctor beforeSaving;
    private Doctor afterSavingToDb;
    private Doctor createANewDoctor;
    private Doctor savedANawDoctorToDb;

    @BeforeEach
    void setupUp() {
        beforeSaving = Doctor.builder()
                .email("pawel@wp.pl")
                .password("pawel123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilities(null)
                .build();
        afterSavingToDb = Doctor.builder()
                .id(1L)
                .email("pawel@wp.pl")
                .password("pawl123")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilities(null)
                .build();
        when(doctorRepository.save(beforeSaving)).thenReturn(afterSavingToDb);

        createANewDoctor = Doctor.builder()
                .email("adam@wp.pl")
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(Specialization.OPHTHALMOLOGY)
                .facilities(null)
                .build();

        savedANawDoctorToDb = Doctor.builder()
                .id(2L)
                .email("adam@wp.pl")
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(Specialization.OPHTHALMOLOGY)
                .facilities(null)
                .build();
    }

    @Test
    void addDoctor_DataCorrect() {
        final Doctor expected = savedANawDoctorToDb;
        when(doctorRepository.findByEmail("test")).thenReturn(Optional.empty());
        when(doctorRepository.save(createANewDoctor)).thenReturn(this.savedANawDoctorToDb);

        doctorService.addDoctor(this.createANewDoctor);
        assertThat(expected.getLastName()).isEqualTo("Nowak");
        assertThat(expected.getId()).isEqualTo(2L);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void addDoctor_DataIncorrect_ThrowDoctorAlreadyExistsException() {
        final String email = "pawel@wp.pl";
        final Doctor aNewDoctor = beforeSaving;
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(this.beforeSaving));
        assertThrows(DoctorAlreadyExistsException.class, () -> doctorService.addDoctor(aNewDoctor));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void addDoctor_DataIncorrect__ThrowInvalidDoctorException() {
        Doctor aNewDoctor = createANewDoctor;
        aNewDoctor.setFirstName(null);
        aNewDoctor.setSpecialization(null);
        when(doctorRepository.findByEmail("test")).thenReturn(Optional.empty());
        assertThrows(InvalidDoctorException.class, () -> doctorService.addDoctor(aNewDoctor));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void getAllDoctor_DataCorrect_MapToDto() {
        Doctor doctor = afterSavingToDb;
        final DoctorDto mapToDoctorDto = doctorMapper.INSTANCE.doctorToDoctorDto(doctor);
        assertThat(mapToDoctorDto).isNotNull();
        assertThat(mapToDoctorDto.getFirstName()).isEqualTo(doctor.getFirstName());
    }

    @Test
    void getAllDoctor_DataCorrect() {
        final Doctor aNewDoctor = createANewDoctor;
        List<DoctorDto> expected = Collections.singletonList(createDoctorDto());
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(aNewDoctor));
        when(doctorMapper.doctorToDoctorDto(aNewDoctor)).thenReturn(createDoctorDto());

        final List<DoctorDto> actual = doctorService.getAllDoctor();
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getFirstName()).isEqualTo("Pawel");
        assertEquals(expected, actual);
    }


    private DoctorDto createDoctorDto() {
        return DoctorDto.builder()
                .email("pawel@wp.pl")
                .firstName("Pawel")
                .lastName("Kowalczyk")
                .specialization(Specialization.FAMILY_MEDICINE)
                .facilities(null)
                .build();
    }
}
