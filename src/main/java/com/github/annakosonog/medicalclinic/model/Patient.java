package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Patient {

    private String email;
    private String password;
    private Long idCardNo;
    private String firstName;
    private String lastName;
    private Integer numberPhone;
    private LocalDate birthday;
}
