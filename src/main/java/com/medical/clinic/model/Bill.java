package com.medical.clinic.model;

import com.medical.clinic.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    private String id;

    private String orderId;
    private String patientId;
    private String appointmentId;

    // NEW: human-readable references, copied from the Appointment at creation time
    private String appointmentNumber;
    private String patientNumber;
    private String patientName;

    private Double consultationFee;
    private Double hospitalCharge;
    private Double amount;
    private String currency;
    private String description;
    private String items;

    private PaymentStatus paymentStatus;

    private String payherePaymentId;
    private String payhereStatusCode;
    private String payhereStatusMessage;

    private String billNumber;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime paidAt;
}