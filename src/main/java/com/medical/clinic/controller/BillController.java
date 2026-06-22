package com.medical.clinic.controller;

import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Bill;
import com.medical.clinic.service.BillService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
@CrossOrigin("*")
@Tag(name = "Billing", description = "Bills and payment records")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<Bill>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>("Bills fetched", false, true, billService.getAllBills()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Bill>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Bill fetched", false, true, billService.getBillById(id)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<Bill>>> byPatient(@PathVariable String patientId) {
        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
        return ResponseEntity.ok(new ApiResponse<>("Bills fetched", false, true, billService.getBillsByPatientId(patientId)));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<Bill>>> byAppointment(@PathVariable String appointmentId) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Bills fetched", false, true, billService.getBillsByAppointmentId(appointmentId)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<Bill>> create(@RequestBody Bill bill) {
        return ResponseEntity.ok(new ApiResponse<>("Bill created", false, true, billService.createBill(bill)));
    }
}
