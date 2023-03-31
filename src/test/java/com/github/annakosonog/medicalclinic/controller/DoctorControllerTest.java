package com.github.annakosonog.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.annakosonog.medicalclinic.model.AssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.SampleAssignDoctorDto;
import com.github.annakosonog.medicalclinic.model.SampleDoctor;
import com.github.annakosonog.medicalclinic.model.SampleDoctorDto;
import com.github.annakosonog.medicalclinic.model.SampleFacility;
import com.github.annakosonog.medicalclinic.model.SampleFacilityDto;
import org.junit.jupiter.api.AfterEach;
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
public class DoctorControllerTest implements SampleDoctor, SampleAssignDoctorDto, SampleDoctorDto, SampleFacility, SampleFacilityDto {

    private static final String DOCTOR_PATH = "/doctors";
    private static final String ROOT_PATH = "$";

    @Autowired
    private DoctorController doctorController;

    @Autowired
    private FacilityController facilityController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;


    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewDoctorDataCorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH)
                .content(json(aCreateDoctorAdamBeforeSave()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).value("Doctor was added successfully"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewDoctorThrowDataAlreadyExistsException() throws Exception {
        final AssignDoctorDto beforeSave = aCreateDoctorAdamBeforeSave();
        doctorController.addNewDoctor(beforeSave);

        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH)
                .content(json(beforeSave))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Doctor already exists"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewDoctorThrowInvalidDoctorException() throws Exception {
        final AssignDoctorDto doctorWithNull = aCreateANewDoctorWithNull();

        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH)
                .content(json(doctorWithNull))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Invalid doctor data"));
    }

    @WithMockUser(roles = "PATIENT")
    @Test
    void assignDoctorToFacilityDataCorrect() throws Exception {
        final Long doctorId = 2L;
        final Long facilityId = 1L;


        doctorController.addNewDoctor(aCreateDoctorPawelBeforeSave());
        facilityController.addNewFacility(aMedicusDto());

        mockMvc.perform(MockMvcRequestBuilders.post(DOCTOR_PATH + "/" + doctorId)
                .param("facilityId", String.valueOf(facilityId))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("ok"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void getAllDoctorDataCorrect() throws Exception {
        final long doctorId = 2L;
        final long facilityId = 1L;

        doctorController.addNewDoctor(aCreateDoctorPawelBeforeSave());
        facilityController.addNewFacility(aMedicusDto());
        doctorController.assignDoctorToFacility(doctorId, facilityId);

        mockMvc.perform(MockMvcRequestBuilders.get(DOCTOR_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isNotEmpty())
                .andExpect(jsonPath(ROOT_PATH).isArray())
                .andExpect(jsonPath("$[0].firstName").value("Pawel"))
                .andExpect(jsonPath("$[0].facilitiesId").value(1L));
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}
