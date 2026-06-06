package com.medical.clinic.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "pharmacy_medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacyMedicine {

    @Id
    private String id;

    private String medicineName;
    private String genericName;
    private String category;
    private String manufacturer;

    private Integer quantity;
    private Integer lowStockThreshold;

    private Double unitPrice;
    private LocalDate expiryDate;

    @Builder.Default
    private Boolean active = true;
}
