package com.medical.clinic.repository;

import com.medical.clinic.model.MedicalReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalReportRepository extends MongoRepository<MedicalReport, String> {
    List<MedicalReport> findByPatientId(String patientId);
    List<MedicalReport> findByDoctorId(String doctorId);
    List<MedicalReport> findByLabTechnicianId(String labTechnicianId);
}
