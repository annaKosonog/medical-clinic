package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Data
public class Patient extends UserData {

    @Column(unique = true, nullable = false)
    private Long idCardNo;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private Integer numberPhone;
    private LocalDate birthday;

    {
        super.setRole(Role.PATIENT);
    }
}
