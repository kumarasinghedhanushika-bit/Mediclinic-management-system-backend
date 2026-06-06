package com.medical.clinic.controller;

import com.medical.clinic.dto.doctor.DoctorRequest;
import com.medical.clinic.dto.doctor.DoctorResponse;
import com.medical.clinic.dto.doctor.TimeSlotResponse;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.User;
import com.medical.clinic.security.SecurityUtils;
import com.medical.clinic.service.DoctorService;
import com.medical.clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("*")
@Tag(name = "Doctors", description = "Doctor management for channeling")
public class DoctorController {

    private final DoctorService doctorService;
    private final UserService userService;

    public DoctorController(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    @Operation(summary = "List all doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>("Doctors fetched", false, true, doctorService.getAllDoctors()));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE', 'PATIENT')")
    @Operation(summary = "List active doctors")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> getActive() {
        return ResponseEntity.ok(new ApiResponse<>("Active doctors fetched", false, true, doctorService.getActiveDoctors()));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> byDepartment(@PathVariable String departmentId) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Doctors fetched",
                false,
                true,
                doctorService.getDoctorsByDepartment(departmentId)
        ));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Current doctor profile")
    public ResponseEntity<ApiResponse<DoctorResponse>> myProfile() {
        User user = userService.getUserByEmail(SecurityUtils.currentUserEmail());
        return ResponseEntity.ok(new ApiResponse<>(
                "Doctor profile fetched",
                false,
                true,
                doctorService.getDoctorByUserId(user.getId())
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DoctorResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Doctor fetched", false, true, doctorService.getDoctorById(id)));
    }

    @GetMapping("/{id}/slots")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Available time slots for a doctor on a date")
    public ResponseEntity<ApiResponse<List<TimeSlotResponse>>> slots(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Slots fetched",
                false,
                true,
                doctorService.getAvailableSlots(id, date)
        ));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create doctor profile")
    public ResponseEntity<ApiResponse<DoctorResponse>> create(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(new ApiResponse<>("Doctor created", false, true, doctorService.createDoctor(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<DoctorResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody DoctorRequest request
    ) {
        return ResponseEntity.ok(new ApiResponse<>("Doctor updated", false, true, doctorService.updateDoctor(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(new ApiResponse<>("Doctor deleted", false, true, null));
    }
}
