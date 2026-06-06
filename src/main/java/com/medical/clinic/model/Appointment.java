package com.medical.clinic.model;

import com.medical.clinic.enums.AppointmentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Document(collection = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    private String id;

    private String appointmentNumber;

    private String patientId;
    private String patientUserId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;

    private String doctorId;
    private String doctorUserId;
    private String doctorName;

    private String departmentId;
    private String departmentName;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDateTime appointmentDateTime;

    private String reason;
    private AppointmentStatus status;
    private String notes;

    private Double consultationFee;

    @Builder.Default
    private Boolean reminderSent = false;

    private String bookedByUserId;
    private String bookedByRole;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
