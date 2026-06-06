package com.medical.clinic.service;

import com.medical.clinic.dto.report.MedicalReportRequest;
import com.medical.clinic.dto.report.MedicalReportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MedicalReportService {
    MedicalReportResponse createReport(MedicalReportRequest request, MultipartFile file, String uploaderEmail);
    MedicalReportResponse updateReport(String id, MedicalReportRequest request, MultipartFile file, String uploaderEmail);
    MedicalReportResponse getById(String id);
    List<MedicalReportResponse> getByPatientId(String patientId);
    List<MedicalReportResponse> getAll();
    void deleteReport(String id);
    byte[] downloadReportFile(String id);
}
