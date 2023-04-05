package com.github.annakosonog.medicalclinic.repository;

import com.github.annakosonog.medicalclinic.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserData, Long> {
    Optional<UserData> findByEmail(String email);
}
