package com.github.annakosonog.medicalclinic.model;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@SuperBuilder
@Setter
@Entity
public class Admin extends UserData {
}
