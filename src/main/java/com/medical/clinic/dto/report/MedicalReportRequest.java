package com.medical.clinic.dto.report;

import com.medical.clinic.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicalReportRequest {

    @NotBlank(message = "Patient id is required")
    private String patientId;

    private String doctorId;
    private String labTechnicianId;

    @NotNull(message = "Report type is required")
    private ReportType reportType;

    private String diagnosis;
    private String notes;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getLabTechnicianId() {
        return labTechnicianId;
    }

    public void setLabTechnicianId(String labTechnicianId) {
        this.labTechnicianId = labTechnicianId;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
