package com.medical.clinic.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord {

    @Id
    private String id;

    private String patientId;

    private String doctorId;

    private String diagnosis;

    private List<String> symptoms;

    private List<String> prescriptions;

    private List<String> labTests;

    private String notes;

    private LocalDateTime visitDate;
}