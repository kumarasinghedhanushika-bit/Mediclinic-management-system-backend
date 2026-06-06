package com.medical.clinic.service;

import com.medical.clinic.model.Patient;
import com.medical.clinic.model.User;

import java.util.List;

public interface PatientService {
    Patient createPatient(Patient patient);
    Patient updatePatient(String id, Patient patient);
    void deletePatient(String id);
    Patient getPatientById(String id);
    List<Patient> getAllPatients();
    Patient getOrCreateForUser(User user);
}
