package com.github.annakosonog.medicalclinic.repository;

import com.github.annakosonog.medicalclinic.model.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
    Optional<Facility> findByName(String name);
}
