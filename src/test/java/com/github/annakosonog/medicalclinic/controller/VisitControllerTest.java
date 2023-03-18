package com.github.annakosonog.medicalclinic.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.model.Visit;
import com.github.annakosonog.medicalclinic.model.VisitDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VisitControllerTest {

    private static final String VISITS_PATH = "/visits";
    private static final String ROOT_PATH = "$";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VisitController visitController;

    @Autowired
    private PatientController patientController;

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewVisitDataCorrect() throws Exception {
        mockMvc.perform(post(VISITS_PATH)
                .content(json(createLocalDateTime()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isNotEmpty());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewVisitThrowIncorrectVisitExceptionDateBefore() throws Exception {
        final LocalDateTime dateTime = LocalDateTime.now().minusDays(3L);
        mockMvc.perform(MockMvcRequestBuilders.post(VISITS_PATH)
                .content(json(dateTime))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Incorrect date"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewVisitThrowIncorrectVisitExceptionIncorrectTime() throws Exception {
        final LocalDateTime dateTime = LocalDateTime.now().plusDays(2L).withMinute(12);
        mockMvc.perform(MockMvcRequestBuilders.post(VISITS_PATH)
                .content(json(dateTime))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Incorrect time"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void addNewVisitThrowIncorrectVisitExceptionDateIsExists() throws Exception {
        final Visit newVisit = createVisit();
        visitController.addNewVisit(newVisit.getTerm()).getBody();

        mockMvc.perform(MockMvcRequestBuilders.post(VISITS_PATH)
                .content(json(createLocalDateTime()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Date already exists"));
    }

    @WithMockUser(roles = "PATIENT")
    @Test
    void appointmentOfThePatientWithDateCorrect() throws Exception {
        final long id = 1L;
        PatientDTO patientDTO = buildPatientDto();
        visitController.addNewVisit(createLocalDateTime());
        patientController.addPatient(createPatient());

        mockMvc.perform(MockMvcRequestBuilders.put(VISITS_PATH + "/" + id)
                .content(json(patientDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(jsonPath(ROOT_PATH).value("The visit has been arranged"));
    }

    @WithMockUser(roles = "PATIENT")
    @Test
    void appointmentOfThePatientThrowVisitNotFoundException() throws Exception {
        final long id = 5L;
        PatientDTO patientDTO = buildPatientDto();

        visitController.addNewVisit(createLocalDateTime());
        mockMvc.perform(MockMvcRequestBuilders.put(VISITS_PATH + "/" + id)
                .content(json(patientDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(jsonPath(ROOT_PATH).value("Visit not found"));
    }

    @WithMockUser(roles = "PATIENT")
    @Test
    void appointmentOfThePatientThrowPatientVisitIsUnavailable() throws Exception {
        final long id = 1L;
        PatientDTO patientDTO = aLauraKowalskaDto();
        patientController.addPatient(createPatient());
        visitController.addNewVisit(createLocalDateTime());
        visitController.appointmentOfThePatient(id, buildPatientDto());

        mockMvc.perform(MockMvcRequestBuilders.put(VISITS_PATH + "/" + id)
                .content(json(patientDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(jsonPath(ROOT_PATH).value("Date already taken"));
    }

    @WithMockUser(roles = "PATIENT")
    @Test
    void appointmentOfThePatientThrowPatientNotFoundException() throws Exception {
        final long id = 1L;
        PatientDTO patientDTO = buildPatientDto();
        visitController.addNewVisit(createLocalDateTime());

        mockMvc.perform(MockMvcRequestBuilders.put(VISITS_PATH + "/" + id)
                .content(json(patientDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Patient not found"));
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }

    private static Visit createVisit() {
        return Visit.builder()
                .id(1L)
                .term(createLocalDateTime())
                .patient(createPatient())
                .build();
    }

    private static Patient createPatient() {
        return Patient.builder()
                .email("klara@wp.pl")
                .password("klara123")
                .idCardNo(1234578L)
                .firstName("Klara")
                .lastName("Kowalska")
                .numberPhone(698247158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();
    }

    private static LocalDateTime createLocalDateTime() {
        return LocalDateTime.now().plusDays(4L)
                .withHour(16)
                .withMinute(15)
                .withSecond(0)
                .withNano(0);
    }

    private PatientDTO buildPatientDto() {
        return PatientDTO.builder()
                .email("klara@wp.pl")
                .firstName("Klara")
                .lastName("Kowalska")
                .build();
    }

    private PatientDTO aLauraKowalskaDto() {
        return PatientDTO.builder()
                .email("laura@wp.pl")
                .firstName("Laura")
                .lastName("Kowalska")
                .build();
    }
}
