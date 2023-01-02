package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Patient {

    private String email;
    private String password;
    private long idCardNo;
    private String firstName;
    private String lastName;
    private int numberPhone;
    private LocalDate birthday;
}
