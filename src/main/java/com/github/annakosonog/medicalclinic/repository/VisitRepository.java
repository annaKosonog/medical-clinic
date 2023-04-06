package com.github.annakosonog.medicalclinic.repository;

import com.github.annakosonog.medicalclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    boolean existsByTerm(LocalDateTime localDateTime);

}
