package com.github.annakosonog.medicalclinic.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.annakosonog.medicalclinic.model.FacilityDto;
import com.github.annakosonog.medicalclinic.model.SampleFacility;
import com.github.annakosonog.medicalclinic.model.SampleFacilityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FacilityControllerTest implements SampleFacilityDto, SampleFacility {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    FacilityController facilityController;

    private static final String FACILITY_PATH = "/facilities";
    private static final String ROOT_PATH = "$";


    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewFacilityDataCorrect() throws Exception {
        final FacilityDto createFacility = aMedSanDto();

        mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_PATH)
                .content(json(createFacility))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Facility is added successfully"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewFacilityDataIncorrectThrowDataAlreadyExistsException() throws Exception {
        final FacilityDto createFacility = aMedSanDto();
        facilityController.addNewFacility(createFacility);

        mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_PATH)
                .content(json(createFacility))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Facility already exists"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewFacilityDataIncorrectThrowInvalidDetailsException() throws Exception {
        final FacilityDto createFacility = aMedSanDtoWithNull();

        mockMvc.perform(MockMvcRequestBuilders.post(FACILITY_PATH)
                .content(json(createFacility))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Incorrect data for facility"));
    }

    @WithMockUser(roles = "PATIENT")
    @Test
    void getAllFacilityDataCorrect() throws Exception {
        final FacilityDto createMedSan = aMedSanDtoWithDoctor();
        final FacilityDto createMedicus = aMedicusDto();

        facilityController.addNewFacility(createMedicus);
        facilityController.addNewFacility(createMedSan);

        mockMvc.perform(MockMvcRequestBuilders.get(FACILITY_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isArray())
                .andExpect(jsonPath(ROOT_PATH).isNotEmpty())
                .andExpect(jsonPath("$[0].name").value("Medicus"))
                .andExpect(jsonPath("$[1].name").value("MedSan"));
    }

    private String json(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
