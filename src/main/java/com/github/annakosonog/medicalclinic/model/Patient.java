package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Setter
public class Patient extends UserData {

    @Column(unique = true, nullable = false)
    private Long idCardNo;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    private Integer numberPhone;
    private LocalDate birthday;
    @OneToMany(mappedBy = "patient")
    private List<Visit> visits;

    {
        super.setRole(Role.PATIENT);
    }
}
