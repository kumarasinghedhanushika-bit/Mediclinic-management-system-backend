package com.medical.clinic.mapper;

import com.medical.clinic.dto.pharmacy.PharmacyMedicineRequest;
import com.medical.clinic.dto.pharmacy.PharmacyMedicineResponse;
import com.medical.clinic.model.PharmacyMedicine;
import org.springframework.stereotype.Component;

@Component
public class PharmacyMedicineMapper {

    public PharmacyMedicine toEntity(PharmacyMedicineRequest request) {
        return PharmacyMedicine.builder()
                .medicineName(request.getMedicineName())
                .genericName(request.getGenericName())
                .category(request.getCategory())
                .manufacturer(request.getManufacturer())
                .quantity(request.getQuantity())
                .lowStockThreshold(request.getLowStockThreshold() != null ? request.getLowStockThreshold() : 10)
                .unitPrice(request.getUnitPrice())
                .expiryDate(request.getExpiryDate())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
    }

    public PharmacyMedicineResponse toResponse(PharmacyMedicine medicine) {
        PharmacyMedicineResponse response = new PharmacyMedicineResponse();
        response.setId(medicine.getId());
        response.setMedicineName(medicine.getMedicineName());
        response.setGenericName(medicine.getGenericName());
        response.setCategory(medicine.getCategory());
        response.setManufacturer(medicine.getManufacturer());
        response.setQuantity(medicine.getQuantity());
        response.setLowStockThreshold(medicine.getLowStockThreshold());
        response.setUnitPrice(medicine.getUnitPrice());
        response.setExpiryDate(medicine.getExpiryDate());
        response.setActive(medicine.getActive());
        int threshold = medicine.getLowStockThreshold() != null ? medicine.getLowStockThreshold() : 10;
        response.setLowStock(medicine.getQuantity() != null && medicine.getQuantity() <= threshold);
        return response;
    }
}
