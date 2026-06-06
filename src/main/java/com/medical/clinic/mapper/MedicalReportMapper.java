package com.medical.clinic.mapper;

import com.medical.clinic.dto.report.MedicalReportRequest;
import com.medical.clinic.dto.report.MedicalReportResponse;
import com.medical.clinic.model.MedicalReport;
import org.springframework.stereotype.Component;

@Component
public class MedicalReportMapper {

    public MedicalReport toEntity(MedicalReportRequest request) {
        return MedicalReport.builder()
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .labTechnicianId(request.getLabTechnicianId())
                .reportType(request.getReportType())
                .diagnosis(request.getDiagnosis())
                .notes(request.getNotes())
                .build();
    }

    public MedicalReportResponse toResponse(MedicalReport report) {
        MedicalReportResponse response = new MedicalReportResponse();
        response.setId(report.getId());
        response.setPatientId(report.getPatientId());
        response.setDoctorId(report.getDoctorId());
        response.setLabTechnicianId(report.getLabTechnicianId());
        response.setReportType(report.getReportType());
        response.setDiagnosis(report.getDiagnosis());
        response.setNotes(report.getNotes());
        response.setReportFileUrl(report.getReportFileUrl());
        response.setCreatedDate(report.getCreatedDate());
        response.setUpdatedAt(report.getUpdatedAt());
        return response;
    }
}
