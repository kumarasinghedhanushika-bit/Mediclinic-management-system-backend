package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.dto.pharmacy.PharmacyMedicineRequest;
import com.medical.clinic.dto.pharmacy.PharmacyMedicineResponse;
import com.medical.clinic.mapper.PharmacyMedicineMapper;
import com.medical.clinic.model.PharmacyMedicine;
import com.medical.clinic.repository.PharmacyMedicineRepository;
import com.medical.clinic.service.EmailServise;
import com.medical.clinic.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyMedicineRepository repository;
    private final PharmacyMedicineMapper mapper;

    @Autowired
    private EmailServise emailServise;

    public PharmacyServiceImpl(PharmacyMedicineRepository repository, PharmacyMedicineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public PharmacyMedicineResponse addMedicine(PharmacyMedicineRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public PharmacyMedicineResponse updateMedicine(String id, PharmacyMedicineRequest request) {
        PharmacyMedicine existing = getEntity(id);
        existing.setMedicineName(request.getMedicineName());
        existing.setGenericName(request.getGenericName());
        existing.setCategory(request.getCategory());
        existing.setManufacturer(request.getManufacturer());
        existing.setQuantity(request.getQuantity());
        if (request.getLowStockThreshold() != null) {
            existing.setLowStockThreshold(request.getLowStockThreshold());
        }
        existing.setUnitPrice(request.getUnitPrice());
        existing.setExpiryDate(request.getExpiryDate());
        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }
        return mapper.toResponse(repository.save(existing));
    }

    @Override
    public void deleteMedicine(String id) {
        repository.deleteById(id);
    }

    @Override
    public PharmacyMedicineResponse getById(String id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public List<PharmacyMedicineResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<PharmacyMedicineResponse> search(String query) {
        if (query == null || query.isBlank()) {
            return getAll();
        }
        Set<PharmacyMedicine> results = new LinkedHashSet<>();
        results.addAll(repository.findByMedicineNameContainingIgnoreCase(query));
        results.addAll(repository.findByGenericNameContainingIgnoreCase(query));
        results.addAll(repository.findByCategoryIgnoreCase(query));
        return results.stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<PharmacyMedicineResponse> getLowStockAlerts() {
        List<PharmacyMedicineResponse> alerts = new ArrayList<>();
        for (PharmacyMedicine medicine : repository.findAll()) {
            PharmacyMedicineResponse response = mapper.toResponse(medicine);
            if (response.isLowStock()) {
                alerts.add(response);
            }
        }
        return alerts;
    }

    @Override
    public PharmacyMedicineResponse adjustStock(String id, int quantityChange) {
        PharmacyMedicine medicine = getEntity(id);
        int newQty = (medicine.getQuantity() != null ? medicine.getQuantity() : 0) + quantityChange;
        if (newQty < 0) {
            throw new RuntimeException("Insufficient stock");
        }
        medicine.setQuantity(newQty);
        return mapper.toResponse(repository.save(medicine));
    }

    private PharmacyMedicine getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));
    }
}
