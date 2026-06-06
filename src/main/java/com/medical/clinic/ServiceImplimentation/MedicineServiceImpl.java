package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.model.Medicine;
import com.medical.clinic.repository.MedicineRepository;
import com.medical.clinic.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    @Override
    public Medicine issueMedicine(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine updateIssue(String id, Medicine medicine) {
        Medicine existing = getIssueById(id);
        existing.setMedicines(medicine.getMedicines());
        existing.setIssuedDate(medicine.getIssuedDate());
        return medicineRepository.save(existing);
    }

    @Override
    public void deleteIssue(String id) {
        medicineRepository.deleteById(id);
    }

    @Override
    public Medicine getIssueById(String id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine issue record not found"));
    }

    @Override
    public List<Medicine> getAllIssues() {
        return medicineRepository.findAll();
    }

    @Override
    public List<Medicine> getIssuesByPatientId(String patientId) {
        return medicineRepository.findByPatientId(patientId);
    }
}
