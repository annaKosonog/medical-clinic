package com.github.annakosonog.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.annakosonog.medicalclinic.model.Doctor;
import com.github.annakosonog.medicalclinic.model.Specialization;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class DoctorControllerTest {

    private static final String DOCTOR_PATH = "/doctors";
    private static final String ROOT_PATH = "$";

    @Autowired
    private DoctorController doctorController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewDoctorDataCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH)
                .content(json(createANewDoctor()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).value("Doctor was added successfully"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewDoctorThrowDoctorAlreadyExistsException() throws Exception {
        final Doctor aNewDoctor = createANewDoctor();
        doctorController.addNewDoctor(aNewDoctor);

        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH)
                .content(json(aNewDoctor))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Doctor already exists"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewDoctorThrowInvalidDoctorException() throws Exception {
        Doctor aNewDoctor = createANewDoctor();
        aNewDoctor.setFirstName(null);
        aNewDoctor.setLastName(null);

        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH)
                .content(json(aNewDoctor))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Invalid doctor data"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllDoctorDataCorrect() throws Exception {
        final Doctor aNewDoctor = createANewDoctor();
        doctorController.addNewDoctor(aNewDoctor);

        mockMvc.perform(MockMvcRequestBuilders.get(DOCTOR_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isNotEmpty())
                .andExpect(jsonPath(ROOT_PATH).isArray())
                .andExpect(jsonPath("$[0].firstName").value("adam@wp.pl"));
    }


    private Doctor createANewDoctor() {
        return Doctor.builder()
                .email("adam@wp.pl")
                .password("adam123")
                .firstName("Adam")
                .lastName("Nowak")
                .specialization(Specialization.OPHTHALMOLOGY)
                .facilities(null)
                .build();
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}
