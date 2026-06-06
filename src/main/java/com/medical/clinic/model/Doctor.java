package com.medical.clinic.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    private String id;

    private String userId;

    private String specialization;

    private String departmentId;

    private String licenseNumber;

    private Integer experienceYears;

    private List<String> availableDays;

    private String consultationStartTime;

    private String consultationEndTime;

    private Integer slotDurationMinutes;

    private Double consultationFee;

    @Builder.Default
    private Boolean active = true;
}
