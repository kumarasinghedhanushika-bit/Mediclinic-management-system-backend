package com.medical.clinic.repository;

import com.medical.clinic.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {

    Patient findByPatientNumber(String patientNumber);

    Patient findByUserId(String userId);
}
