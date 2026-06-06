package com.medical.clinic.service;

import com.medical.clinic.model.Medicine;
import java.util.List;

public interface MedicineService {
    Medicine issueMedicine(Medicine medicine);
    Medicine updateIssue(String id, Medicine medicine);
    void deleteIssue(String id);
    Medicine getIssueById(String id);
    List<Medicine> getAllIssues();
    List<Medicine> getIssuesByPatientId(String patientId);
}
