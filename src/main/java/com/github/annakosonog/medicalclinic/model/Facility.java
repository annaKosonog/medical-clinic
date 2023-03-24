package com.github.annakosonog.medicalclinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String city;
    private String postCode;
    private String street;
    private String number;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST},
            mappedBy = "facilities")
    private List<Doctor> doctor;

}
