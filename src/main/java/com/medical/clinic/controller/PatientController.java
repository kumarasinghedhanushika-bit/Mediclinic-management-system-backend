package com.medical.clinic.controller;

import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Patient;
import com.medical.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin("*")

public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Patient>>> getAllPatients() {
        return ResponseEntity.ok(new ApiResponse<>("Patients fetched successfully", false, true, patientService.getAllPatients()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Patient>> getPatientById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Patient fetched successfully", false, true, patientService.getPatientById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Patient>> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(new ApiResponse<>("Patient created successfully", false, true, patientService.createPatient(patient)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Patient>> updatePatient(@PathVariable String id, @RequestBody Patient patient) {
        return ResponseEntity.ok(new ApiResponse<>("Patient updated successfully", false, true, patientService.updatePatient(id, patient)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(new ApiResponse<>("Patient deleted successfully", false, true, null));
    }
}
