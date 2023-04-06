package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.DataAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.InvalidDetailsException;
import com.github.annakosonog.medicalclinic.mapper.DoctorMapper;
import com.github.annakosonog.medicalclinic.model.AssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.DoctorDto;
import com.github.annakosonog.medicalclinic.model.SampleAssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.SampleDoctor;
import com.github.annakosonog.medicalclinic.model.SampleDoctorDto;
import com.github.annakosonog.medicalclinic.model.SampleFacility;
import com.github.annakosonog.medicalclinic.repository.DoctorRepository;
import com.github.annakosonog.medicalclinic.repository.FacilityRepository;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DoctorServiceTest implements SampleDoctor, SampleAssignDoctorDto, SampleFacility, SampleDoctorDto {

    @Mock
    DoctorRepository doctorRepository;

    @Mock
    FacilityRepository facilityRepository;

    @Mock
    DoctorMapper doctorMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    DoctorService doctorService;

    @AfterEach
    void cleanUp() {
        doctorRepository.deleteAll();
        facilityRepository.deleteAll();
    }

    @Test
    void addDoctor_DataCorrect_DoctorSaved() {
        when(doctorRepository.save(createAdam())).thenReturn(savedAdamToDb());
        when(doctorRepository.findByEmail("adam@wp.pl")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("adam123")).thenReturn("adam123");
        when(doctorMapper.assignDoctorDtoToDoctor(aCreateDoctorAdamBeforeSave())).thenReturn(savedAdamToDb());

        doctorService.addDoctor(aCreateDoctorAdamBeforeSave());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void addDoctor_DataIncorrect_ThrowDataAlreadyExistsException() {
        when(doctorRepository.save(beforeSavingAPawel())).thenReturn(afterSavingAPawelToDb());
        final String email = "pawel@wp.pl";
        when(doctorRepository.findByEmail(email)).thenReturn(Optional.of(beforeSavingAPawel()));
        assertThrows(DataAlreadyExistsException.class, () -> doctorService.addDoctor(doctorPawelAlreadySaveToDb()));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void addDoctor_DataIncorrect__ThrowInvalidDoctorException() {
        final AssignDoctorDto doctorWithNull = aCreateANewDoctorWithNull();
        when(doctorRepository.findByEmail("test")).thenReturn(Optional.empty());
        assertThrows(InvalidDetailsException.class, () -> doctorService.addDoctor(doctorWithNull));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void getAllDoctor_DataCorrect_MapToDto() {
        final AssignDoctorDto assignDoctorDto = aCreateDoctorAdamBeforeSave();
        Doctor doctor = createAdam();
        final Doctor mapToDoctor = doctorMapper.INSTANCE.assignDoctorDtoToDoctor(assignDoctorDto);
        assertThat(mapToDoctor).isNotNull();
        assertThat(mapToDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    }

    @Test
    void getAllDoctor_DataCorrect() {
        when(doctorRepository.save(beforeSavingAPawel())).thenReturn(afterSavingAPawelToDb());
        when(facilityRepository.save(beforeSaveMedicus())).thenReturn(afterSavedMedicus());
        List<DoctorDto> expected = Collections.singletonList(createAPawelDoctorDto());
        when(doctorRepository.findAll()).thenReturn(Collections.singletonList(aPawelToDbWithMedicusFacility()));

        final List<DoctorDto> actual = doctorService.getAllDoctor();
        assertThat(actual).isNotNull();
        assertThat(actual.get(0).getFirstName()).isEqualTo("Pawel");
        assertEquals(expected.get(0).getFacilitiesId(), actual.get(0).getFacilitiesId());
    }

    @Test
    void assignDoctorToFacility_DataCorrect_savedFacilityToDoctor() {
        final Long doctorId = 1L;
        final Long facilityId = 1L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(aPawelToDbWithMedicusFacility()));
        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(afterSavedMedicus()));
        when(doctorRepository.save(afterSavingAPawelToDb())).thenReturn(aPawelToDbWithMedicusFacility());

        final String actual = doctorService.assignDoctorToFacility(doctorId, facilityId);
        assertEquals("ok", actual);
    }
}
