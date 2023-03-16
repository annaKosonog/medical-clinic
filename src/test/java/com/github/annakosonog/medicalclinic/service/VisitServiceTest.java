package com.github.annakosonog.medicalclinic.service;

import com.github.annakosonog.medicalclinic.exception.visit.IncorrectVisitException;
import com.github.annakosonog.medicalclinic.mapper.VisitMapper;
import com.github.annakosonog.medicalclinic.model.Visit;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import com.github.annakosonog.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.time.LocalDateTime;
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

    @Test
    public void createNewVisit_DataCorrect_CreateDate() {
        LocalDateTime dateTime = LocalDateTime.of(2222, 3, 16, 16, 15);
        visitService.createNewVisit(dateTime);
        verify(visitRepository, times(1)).save(any(Visit.class));
        assertThat(visitRepository.existsByTerm(dateTime));
    }

    @Test
    public void createNewVisit_DataIncorrect_IncorrectDate() {
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(1L);
        assertThrows(IncorrectVisitException.class, () -> visitService.createNewVisit(beforeDate));
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    public void createNewVisit_DataIncorrect_IncorrectTime() {
        final LocalDateTime dateTime = LocalDateTime.now().withMinute(17);
        assertThrows(IncorrectVisitException.class, () -> visitService.createNewVisit(dateTime));
    }

    @Test
    public void createNewVisit_DataIncorrect_DateAlreadyExists() {
        final Visit savedVisit = buildVisit();
        when(visitRepository.save(savedVisit)).thenReturn(savedVisit);
        assertThrows(IncorrectVisitException.class, () -> visitService.createNewVisit(buildVisit().getTerm()));
    }

    @Test
    public void  createNewVisit_DataCorrect_MapToDto(){
        Visit visit = buildVisit();
        VisitDTO mapToVisitDto = visitMapper.INSTANCE.visitToVisitDto(visit);
        assertThat(mapToVisitDto).isNotNull();
        assertThat(mapToVisitDto.getTerm()).isEqualTo(buildVisit().getTerm());
    }

    private Visit buildVisit() {
        LocalDateTime localDateTime = LocalDateTime.now().withMinute(15).withSecond(0).withNano(0);
        return Visit.builder()
                .term(localDateTime)
                .id(1L)
                .patient(null)
                .build();
    }
}

