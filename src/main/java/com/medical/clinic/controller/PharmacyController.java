package com.medical.clinic.controller;

import com.medical.clinic.dto.pharmacy.PharmacyMedicineRequest;
import com.medical.clinic.dto.pharmacy.PharmacyMedicineResponse;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@CrossOrigin("*")
@Tag(name = "Pharmacy", description = "Medicine inventory and stock management")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @PostMapping("/medicines")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    @Operation(summary = "Add medicine to inventory")
    public ResponseEntity<ApiResponse<PharmacyMedicineResponse>> add(
            @Valid @RequestBody PharmacyMedicineRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>("Medicine added", false, true, pharmacyService.addMedicine(request)));
    }

    @PutMapping("/medicines/{id}")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<PharmacyMedicineResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody PharmacyMedicineRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>("Medicine updated", false, true, pharmacyService.updateMedicine(id, request)));
    }

    @DeleteMapping("/medicines/{id}")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        pharmacyService.deleteMedicine(id);
        return ResponseEntity.ok(new ApiResponse<>("Medicine deleted", false, true, null));
    }

    @GetMapping("/medicines/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PharmacyMedicineResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Medicine fetched", false, true, pharmacyService.getById(id)));
    }

    @GetMapping("/medicines")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PharmacyMedicineResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>("Medicines fetched", false, true, pharmacyService.getAll()));
    }

    @GetMapping("/medicines/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Search medicines by name, generic name, or category")
    public ResponseEntity<ApiResponse<List<PharmacyMedicineResponse>>> search(@RequestParam String q) {
        return ResponseEntity.ok(new ApiResponse<>("Search results", false, true, pharmacyService.search(q)));
    }

    @GetMapping("/medicines/low-stock")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    @Operation(summary = "Low stock alerts")
    public ResponseEntity<ApiResponse<List<PharmacyMedicineResponse>>> lowStock() {
        return ResponseEntity.ok(new ApiResponse<>("Low stock medicines", false, true, pharmacyService.getLowStockAlerts()));
    }

    @PatchMapping("/medicines/{id}/stock")
    @PreAuthorize("hasAnyRole('PHARMACIST', 'ADMIN')")
    @Operation(summary = "Adjust stock (+/- quantity)")
    public ResponseEntity<ApiResponse<PharmacyMedicineResponse>> adjustStock(
            @PathVariable String id,
            @RequestParam int change
    ) {
        return ResponseEntity.ok(new ApiResponse<>("Stock updated", false, true, pharmacyService.adjustStock(id, change)));
    }
}
