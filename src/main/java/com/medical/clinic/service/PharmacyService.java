package com.medical.clinic.service;

import com.medical.clinic.dto.pharmacy.PharmacyMedicineRequest;
import com.medical.clinic.dto.pharmacy.PharmacyMedicineResponse;

import java.util.List;

public interface PharmacyService {
    PharmacyMedicineResponse addMedicine(PharmacyMedicineRequest request);
    PharmacyMedicineResponse updateMedicine(String id, PharmacyMedicineRequest request);
    void deleteMedicine(String id);
    PharmacyMedicineResponse getById(String id);
    List<PharmacyMedicineResponse> getAll();
    List<PharmacyMedicineResponse> search(String query);
    List<PharmacyMedicineResponse> getLowStockAlerts();
    PharmacyMedicineResponse adjustStock(String id, int quantityChange);
}
