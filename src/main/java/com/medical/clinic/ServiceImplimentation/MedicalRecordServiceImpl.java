package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.model.MedicalRecord;
import com.medical.clinic.repository.MedicalRecordRepository;
import com.medical.clinic.service.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord record) {
        return medicalRecordRepository.save(record);
    }

    @Override
    public MedicalRecord updateMedicalRecord(String id, MedicalRecord record) {
        MedicalRecord existing = getMedicalRecordById(id);
        existing.setDiagnosis(record.getDiagnosis());
        existing.setSymptoms(record.getSymptoms());
        existing.setPrescriptions(record.getPrescriptions());
        existing.setLabTests(record.getLabTests());
        existing.setNotes(record.getNotes());
        return medicalRecordRepository.save(existing);
    }

    @Override
    public void deleteMedicalRecord(String id) {
        medicalRecordRepository.deleteById(id);
    }

    @Override
    public MedicalRecord getMedicalRecordById(String id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found"));
    }

    @Override
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(String patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }
}
