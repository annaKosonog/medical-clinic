package com.github.annakosonog.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {


    private static final String PATIENTS_PATH = "/patients";
    private static final String ROOT_PATH = "$";
    private static final String EMAIL_SUFFIX = ".email";
    private static final String EMAIL_PATH = ROOT_PATH + EMAIL_SUFFIX;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientController patientController;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Optional.ofNullable(patientController.getAllPatients())
                .map(ResponseEntity::getBody)
                .filter(patients -> patients.size() > 0)
                .ifPresent(patients -> patients.forEach(this::removePatient));
    }

    @Test
    void getAllPatients() throws Exception {
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.get(PATIENTS_PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isArray())
                .andExpect(jsonPath("$[0].email").value("klara@wp.pl"))
                .andExpect(jsonPath("$[0].password").value("klara123"));
    }

    @Test
    void getPatient() throws Exception {
        final String email = "/klara@wp.pl";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.get(PATIENTS_PATH + email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isNotEmpty())
                .andExpect(jsonPath(EMAIL_PATH).isString())
                .andExpect(jsonPath(EMAIL_PATH).value("klara@wp.pl"));
    }

    @Test
    void addPatient() throws Exception {
        Patient aKlaraKowalska = createPatient();

        mockMvc.perform(MockMvcRequestBuilders.post(PATIENTS_PATH)
                .content(json(aKlaraKowalska))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).isString())
                .andExpect(jsonPath(ROOT_PATH).value("Patient was added successfully"));

        Patient patient1 = patientController.getPatient("klara@wp.pl").getBody();
        assertEquals(patient1.getEmail(), aKlaraKowalska.getEmail());
        assertEquals(patient1.getFirstName(), aKlaraKowalska.getFirstName());
    }

    @Test
    void addPatientThrowPatientAlreadyExistsException() throws Exception {
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.post(PATIENTS_PATH)
                .content(json(aKlaraKowalska))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Patient already exists"));
    }

    @Test
    void addPatientThrowPatientException() throws Exception {
        Patient patient = createPatient();
        Patient patient2 = createPatient();
        patientController.addPatient(patient);
        patient2.setEmail(null);

        mockMvc.perform(MockMvcRequestBuilders.post(PATIENTS_PATH)
                .content(json(patient2))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Invalid patient data"));
    }

    @Test
    void deletePatient() throws Exception {
        final String email = "/klara@wp.pl";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.delete(PATIENTS_PATH + email))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(EMAIL_PATH).doesNotExist())
                .andExpect(jsonPath(ROOT_PATH).value("Patient was deleted successfully"));
    }

    @Test
    void updatePatient() throws Exception {
        final String email = "/klara@wp.pl";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.put(PATIENTS_PATH + email)
                .content(json(updateKlara()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).value("Patient was update successfully"));

        Patient patient = patientController.getPatient("laura@wp.pl").getBody();
        assertEquals(patient.getFirstName(), "Laura");
        assertEquals(patient.getEmail(), "laura@wp.pl");
    }

    @Test
    void updatePatientThrowPatientNotFoundException() throws Exception {
        final String email = "/email";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        aKlaraKowalska.setFirstName("Laura");
        aKlaraKowalska.setIdCardNo(1255578L);
        aKlaraKowalska.setNumberPhone(517358158);

        mockMvc.perform(MockMvcRequestBuilders.put(PATIENTS_PATH + email)
                .content(json(aKlaraKowalska))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Patient not found"));
    }

    @Test
    void updatePatientThrowingPatientException() throws Exception {
        final String email = "/klara@wp.pl";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        updateKlara().setIdCardNo(1234L);

        mockMvc.perform(MockMvcRequestBuilders.put(PATIENTS_PATH + email)
                .content(json(updateKlara()))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Do not change card number"));
    }

    @Test
    void updatePatientThrowingInvalidPatientDataException() throws Exception {
        final String email = "/klara@wp.pl";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        Patient editKlara = updateKlara();
        editKlara.setFirstName(null);
        editKlara.setPassword(null);
        editKlara.setBirthday(null);

        mockMvc.perform(MockMvcRequestBuilders.put(PATIENTS_PATH + email)
                .content(json(editKlara))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Invalid patient data"));
    }

    @Test
    void editPatientPassword() throws Exception {
        final String email = "/klara@wp.pl";
        final String password = "klara124";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATIENTS_PATH + email)
                .content(json(password))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(ROOT_PATH).value("Password changed successfully"));

        Patient actual = patientController.getPatient("klara@wp.pl").getBody();
        assertEquals(json(password), actual.getPassword());
        assertEquals(actual.getEmail(), "klara@wp.pl");
        assertEquals(actual.getNumberPhone(), 698247158);
    }

    @Test
    void editPatientPasswordThrowPatientNotFoundException() throws Exception {
        final String password = "klara124";
        final String email = "/email";
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATIENTS_PATH + email)
                .content(json(password))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Patient not found"));
    }

    @Test
    void editPatientPasswordThrowingInvalidPatientDataException() throws Exception {
        final String email = "/klara@wp.pl";
        final String password = null;
        Patient aKlaraKowalska = createPatient();
        patientController.addPatient(aKlaraKowalska);

        mockMvc.perform(MockMvcRequestBuilders.patch(PATIENTS_PATH + email)
                .content(json(password))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(ROOT_PATH).value("Invalid patient data"));
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

    private static Patient updateKlara() {
        return Patient.builder()
                .email("laura@wp.pl")
                .password("laura124")
                .idCardNo(1234578L)
                .firstName("Laura")
                .lastName("Kowalska")
                .numberPhone(517358158)
                .birthday(LocalDate.of(2000, 10, 15))
                .build();
    }

    private void removePatient(Patient patient) {
        patientController.deletePatient(patient.getEmail());
    }

    private String json(Object o) throws IOException {
        return objectMapper.writeValueAsString(o);
    }
}
