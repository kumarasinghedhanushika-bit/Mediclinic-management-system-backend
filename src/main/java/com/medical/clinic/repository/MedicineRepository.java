package com.medical.clinic.repository;

import com.medical.clinic.model.Medicine;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MedicineRepository extends MongoRepository<Medicine, String> {
    List<Medicine> findByPatientId(String patientId);
    List<Medicine> findByPrescriptionId(String prescriptionId);
}
