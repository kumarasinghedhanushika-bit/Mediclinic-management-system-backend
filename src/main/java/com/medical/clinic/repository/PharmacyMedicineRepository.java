package com.medical.clinic.repository;

import com.medical.clinic.model.PharmacyMedicine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PharmacyMedicineRepository extends MongoRepository<PharmacyMedicine, String> {
    List<PharmacyMedicine> findByMedicineNameContainingIgnoreCase(String name);
    List<PharmacyMedicine> findByGenericNameContainingIgnoreCase(String name);
    List<PharmacyMedicine> findByCategoryIgnoreCase(String category);
    List<PharmacyMedicine> findByQuantityLessThanEqual(Integer threshold);
}
