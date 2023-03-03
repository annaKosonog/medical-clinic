package com.github.annakosonog.medicalclinic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MedicalClinicApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalClinicApplication.class, args);
    }

}
