package com.medical.clinic.service;

import com.medical.clinic.model.MedicalRecord;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecord createMedicalRecord(MedicalRecord record);
    MedicalRecord updateMedicalRecord(String id, MedicalRecord record);
    void deleteMedicalRecord(String id);
    MedicalRecord getMedicalRecordById(String id);
    List<MedicalRecord> getAllMedicalRecords();
    List<MedicalRecord> getMedicalRecordsByPatientId(String patientId);
}
