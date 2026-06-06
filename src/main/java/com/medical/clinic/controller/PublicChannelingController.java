package com.medical.clinic.controller;

import com.medical.clinic.dto.doctor.DoctorResponse;
import com.medical.clinic.dto.doctor.TimeSlotResponse;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Department;
import com.medical.clinic.service.DepartmentService;
import com.medical.clinic.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@CrossOrigin("*")
@Tag(name = "Public Channeling", description = "Public endpoints for doctor channeling website")
public class PublicChannelingController {

    private final DoctorService doctorService;
    private final DepartmentService departmentService;

    public PublicChannelingController(DoctorService doctorService, DepartmentService departmentService) {
        this.doctorService = doctorService;
        this.departmentService = departmentService;
    }

    @GetMapping("/departments")
    @Operation(summary = "List departments for channeling website")
    public ResponseEntity<ApiResponse<List<Department>>> departments() {
        return ResponseEntity.ok(new ApiResponse<>(
                "Departments fetched",
                false,
                true,
                departmentService.getAllDepartments()
        ));
    }

    @GetMapping("/doctors")
    @Operation(summary = "List active doctors for channeling website")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> doctors(
            @RequestParam(required = false) String departmentId
    ) {
        List<DoctorResponse> doctors = departmentId != null && !departmentId.isBlank()
                ? doctorService.getDoctorsByDepartment(departmentId)
                : doctorService.getActiveDoctors();
        return ResponseEntity.ok(new ApiResponse<>("Doctors fetched", false, true, doctors));
    }

    @GetMapping("/doctors/{id}")
    @Operation(summary = "Doctor details for channeling website")
    public ResponseEntity<ApiResponse<DoctorResponse>> doctor(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Doctor fetched", false, true, doctorService.getDoctorById(id)));
    }

    @GetMapping("/doctors/{id}/slots")
    @Operation(summary = "Available slots (public browse; booking requires login)")
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
}
