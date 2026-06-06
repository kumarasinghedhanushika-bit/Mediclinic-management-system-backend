package com.medical.clinic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "medicine_issues")
public class Medicine {

    @Id
    private String id;

    private String prescriptionId;
    private String patientId;
    private String pharmacistId;

    private String medicines; // issued medicines
    private String issuedDate;
}