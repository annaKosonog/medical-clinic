package com.github.annakosonog.medicalclinic.service;
import com.github.annakosonog.medicalclinic.exception.patient.InvalidPatientDataException;
import com.github.annakosonog.medicalclinic.exception.patient.PatientAlreadyExistsException;
import com.github.annakosonog.medicalclinic.exception.patient.PatientException;
import com.github.annakosonog.medicalclinic.exception.patient.PatientNotFoundException;
import com.github.annakosonog.medicalclinic.mapper.PatientMapper;
import com.github.annakosonog.medicalclinic.model.Patient;
import com.github.annakosonog.medicalclinic.model.PatientDTO;
import com.github.annakosonog.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public List<PatientDTO> getPatientsDto() {
        return getPatients().stream()
                .map(patientMapper::patientToPatientDto)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatient(String email) {
        Patient patient = patientRepository.findByEmail(email).orElseThrow(PatientNotFoundException::new);
        return patientMapper.patientToPatientDto(patient);
    }

    public void addPatient(Patient patient) {
        Optional<Patient> existingPatient = patientRepository.findByEmail(patient.getEmail());
        if (existingPatient.isPresent()) {
            throw new PatientAlreadyExistsException();
        }
        if (patient.getEmail() == null) {
            throw new PatientException("Invalid patient data");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientRepository.save(patient);
    }

    public void deletePatient(String email) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        patientRepository.delete(patient);
    }

    public void updatePatient(Patient patient, String email) {
        Patient entity = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);

        if (!entity.getIdCardNo().equals(patient.getIdCardNo())) {
            throw new PatientException("Do not change card number");
        }
        if (patient.getId() == null) {
            patient.setId(entity.getId());
        }
        if (!isValid(patient)) {
            throw new InvalidPatientDataException("Invalid patient data");
        }
        patientRepository.save(patient);
    }

    public void updatePasswordPatient(String email, String password) {
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(PatientNotFoundException::new);
        if (password == null) {
            throw new NullPointerException("Password not be null");
        }
        patient.setPassword(password);
        patientRepository.save(patient);
    }

    private boolean isValid(Patient patient) {
        return patient.getIdCardNo() != null && patient.getEmail() != null && patient.getFirstName() != null && patient.getLastName() != null
                && patient.getNumberPhone() != null && patient.getPassword() != null && patient.getBirthday() != null;
    }
}
