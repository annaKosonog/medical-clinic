package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.DataAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.InvalidDetailsException;
import com.github.annakosonog.medicalclinic.mapper.FacilityMapper;
import com.github.annakosonog.medicalclinic.model.Facility;
import com.github.annakosonog.medicalclinic.model.FacilityDto;
import com.github.annakosonog.medicalclinic.model.SampleFacility;
import com.github.annakosonog.medicalclinic.model.SampleFacilityDto;
import com.github.annakosonog.medicalclinic.repository.FacilityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(value = MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FacilityServiceTest implements SampleFacilityDto, SampleFacility {

    @Mock
    FacilityRepository facilityRepository;

    @Mock
    FacilityMapper facilityMapper;

    @InjectMocks
    FacilityService facilityService;

    @Test
    void findAllFacility_DataCorrect_ReturnAllFacility() {
        when(facilityRepository.save(beforeSaveMedicus())).thenReturn(afterSavedMedicus());
        when(facilityRepository.findAll()).thenReturn(Collections.singletonList(afterSavedMedicus()));

        final List<FacilityDto> actual = facilityService.findAllFacility();

        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getPostCode()).isEqualTo("00-001");
    }

    @Test
    void createANewFacility_DataCorrect_FacilitySaved() {
        final String name = "MedSan";
        when(facilityRepository.findByName(name)).thenReturn(Optional.empty());
        when(facilityMapper.facilityDtoToFacility(aMedSanDto())).thenReturn(createAMedSanBefore());
        when(facilityRepository.save(createAMedSanBefore())).thenReturn(createAMedSanSavedToDb());

        facilityService.createANewFacility(aMedSanDto());
        verify(facilityRepository, times(1)).save(any(Facility.class));
    }

    @Test
    void createANewFacility_DataIncorrect_ThrowDataAlreadyExistsException() {
        final String name = "MedSan";
        final Facility saved = createAMedSanSavedToDb();
        when(facilityRepository.save(createAMedSanBefore())).thenReturn(createAMedSanSavedToDb());
        when(facilityRepository.findByName(name)).thenReturn(Optional.of(saved));
        assertThrows(DataAlreadyExistsException.class, () -> facilityService.createANewFacility(aMedSanDto()));
        verify(facilityRepository, never()).save(any(Facility.class));
    }

    @Test
    void createANewFacility_DataIncorrect_ThrowInvalidDetailsException() {
        final String name = "MedSan";
        when(facilityRepository.findByName(name)).thenReturn(Optional.empty());
        assertThrows(InvalidDetailsException.class, () -> facilityService.createANewFacility(aMedSanDtoWithNull()));
        verify(facilityRepository, never()).save(any(Facility.class));

    }

    @Test
    void createANewFacility_DataCorrect_MapFacilityDtoToFacility() {
        final FacilityDto facilityDto = aMedSanDto();
        final Facility toFacility = FacilityMapper.INSTANCE.facilityDtoToFacility(aMedSanDto());

        assertThat(toFacility).isNotNull();
        assertThat(toFacility.getCity()).isEqualTo(facilityDto.getCity());
        assertThat(toFacility.getName()).isIn("MedSan");
        assertThat(toFacility.getNumber()).isIn("2b");
    }
}
