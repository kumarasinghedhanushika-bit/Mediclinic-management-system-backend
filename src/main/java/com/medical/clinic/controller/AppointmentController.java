package com.medical.clinic.controller;

import com.medical.clinic.dto.appointment.*;
import com.medical.clinic.mapper.AppointmentMapper;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Appointment;
import com.medical.clinic.security.SecurityUtils;
import com.medical.clinic.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin("*")
@Tag(name = "Appointments", description = "Doctor channeling appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentMapper appointmentMapper;

    public AppointmentController(AppointmentService appointmentService, AppointmentMapper appointmentMapper) {
        this.appointmentService = appointmentService;
        this.appointmentMapper = appointmentMapper;
    }

    @PostMapping("/book")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Patient books an appointment online")
    public ResponseEntity<ApiResponse<AppointmentResponse>> book(
            @Valid @RequestBody AppointmentBookRequest request
    ) {
        AppointmentResponse response = appointmentService.bookAppointment(
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment booked successfully", false, true, response));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Patient views own appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> myAppointments() {
        List<AppointmentResponse> list = appointmentService.getMyPatientAppointments(
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointments fetched", false, true, list));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "Cancel appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancel(@PathVariable String id) {
        AppointmentResponse response = appointmentService.cancelAppointment(
                id,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment cancelled", false, true, response));
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "Reschedule appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> reschedule(
            @PathVariable String id,
            @Valid @RequestBody AppointmentRescheduleRequest request
    ) {
        AppointmentResponse response = appointmentService.rescheduleAppointment(
                id,
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment rescheduled", false, true, response));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "Receptionist creates appointment for walk-in patient")
    public ResponseEntity<ApiResponse<AppointmentResponse>> create(
            @Valid @RequestBody AppointmentCreateRequest request
    ) {
        AppointmentResponse response = appointmentService.createAppointment(
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment created", false, true, response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "Receptionist edits appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> update(
            @PathVariable String id,
            @Valid @RequestBody AppointmentCreateRequest request
    ) {
        AppointmentResponse response = appointmentService.updateAppointment(
                id,
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment updated", false, true, response));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "Confirm pending appointment")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirm(@PathVariable String id) {
        AppointmentResponse response = appointmentService.confirmAppointment(
                id,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment confirmed", false, true, response));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Doctor updates appointment status")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody AppointmentStatusUpdateRequest request
    ) {
        AppointmentResponse response = appointmentService.updateStatus(
                id,
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Status updated", false, true, response));
    }

    @GetMapping("/doctor/schedule")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Doctor views assigned appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> doctorSchedule() {
        List<AppointmentResponse> list = appointmentService.getMyDoctorAppointments(
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Schedule fetched", false, true, list));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    @Operation(summary = "List all appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(
                "Appointments fetched",
                false,
                true,
                appointmentService.getAllAppointments()
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get appointment by id")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Appointment fetched",
                false,
                true,
                appointmentService.getAppointmentById(id)
        ));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> byPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Patient appointments fetched",
                false,
                true,
                appointmentService.getAppointmentsByPatientId(patientId)
        ));
    }

    @GetMapping("/number/{appointmentNumber}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get appointment by appointment number")
    public ResponseEntity<ApiResponse<Appointment>> getByAppointmentNumber(@PathVariable String appointmentNumber) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Appointment fetched",
                false,
                true,
                appointmentService.getEntityByAppointmentNumber(appointmentNumber)
        ));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> byDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Doctor appointments fetched",
                false,
                true,
                appointmentService.getAppointmentsByDoctorId(doctorId)
        ));
    }
}
