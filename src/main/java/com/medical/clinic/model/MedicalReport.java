package com.medical.clinic.model;

import com.medical.clinic.enums.ReportType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "medical_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalReport {

    @Id
    private String id;

    private String patientId;
    private String doctorId;
    private String labTechnicianId;

    private ReportType reportType;
    private String diagnosis;
    private String notes;
    private String reportFileUrl;

    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime updatedAt;
}
