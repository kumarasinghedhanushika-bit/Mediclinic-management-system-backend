package com.medical.clinic.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    private String id;

    private String userId;

    private String patientNumber;

    private LocalDate dateOfBirth;

    private String bloodGroup;

    private String allergies;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String address;

    private String medicalHistory;

    private List<String> chronicDiseases;
}