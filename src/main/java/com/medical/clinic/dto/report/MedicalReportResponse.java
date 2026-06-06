package com.medical.clinic.dto.report;

import com.medical.clinic.enums.ReportType;

import java.time.LocalDateTime;

public class MedicalReportResponse {

    private String id;
    private String patientId;
    private String doctorId;
    private String labTechnicianId;
    private ReportType reportType;
    private String diagnosis;
    private String notes;
    private String reportFileUrl;
    private LocalDateTime createdDate;
    private LocalDateTime updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getLabTechnicianId() { return labTechnicianId; }
    public void setLabTechnicianId(String labTechnicianId) { this.labTechnicianId = labTechnicianId; }
    public ReportType getReportType() { return reportType; }
    public void setReportType(ReportType reportType) { this.reportType = reportType; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getReportFileUrl() { return reportFileUrl; }
    public void setReportFileUrl(String reportFileUrl) { this.reportFileUrl = reportFileUrl; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
