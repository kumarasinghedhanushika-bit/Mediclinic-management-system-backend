package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.model.Patient;
import com.medical.clinic.model.User;
import com.medical.clinic.repository.PatientRepository;
import com.medical.clinic.service.PatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(String id, Patient patient) {
        Patient existing = getPatientById(id);
        existing.setDateOfBirth(patient.getDateOfBirth());
        existing.setBloodGroup(patient.getBloodGroup());
        existing.setAllergies(patient.getAllergies());
        existing.setEmergencyContactName(patient.getEmergencyContactName());
        existing.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        existing.setAddress(patient.getAddress());
        existing.setMedicalHistory(patient.getMedicalHistory());
        existing.setChronicDiseases(patient.getChronicDiseases());
        return patientRepository.save(existing);
    }

    @Override
    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }

    @Override
    public Patient getPatientById(String id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getOrCreateForUser(User user) {
        Patient existing = patientRepository.findByUserId(user.getId());
        if (existing != null) {
            return existing;
        }
        Patient patient = Patient.builder()
                .userId(user.getId())
                .patientNumber("PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .build();
        return patientRepository.save(patient);
    }
}
