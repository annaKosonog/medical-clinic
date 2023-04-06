package com.github.annakosonog.medicalclinic.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class VisitDTO {
    private LocalDateTime term;
    private Long patientId;
}
