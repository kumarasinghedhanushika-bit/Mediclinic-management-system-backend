package com.medical.clinic.controller;

import com.medical.clinic.dto.appointment.AppointmentCreateRequest;
import com.medical.clinic.dto.appointment.AppointmentResponse;
import com.medical.clinic.dto.auth.RegisterRequest;
import com.medical.clinic.dto.auth.UserResponse;
import com.medical.clinic.dto.doctor.DoctorResponse;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.model.Patient;
import com.medical.clinic.security.SecurityUtils;
import com.medical.clinic.service.AppointmentService;
import com.medical.clinic.service.DoctorService;
import com.medical.clinic.service.PatientService;
import com.medical.clinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reception")
@CrossOrigin("*")
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
@Tag(name = "Reception", description = "Receptionist desk operations")
public class ReceptionistController {

    private final UserService userService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    public ReceptionistController(
            UserService userService,
            PatientService patientService,
            DoctorService doctorService,
            AppointmentService appointmentService
    ) {
        this.userService = userService;
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/walk-in/register")
    @Operation(summary = "Register walk-in patient")
    public ResponseEntity<ApiResponse<UserResponse>> registerWalkIn(
            @Valid @RequestBody RegisterRequest request
    ) {
        if (request.getRole() == null) {
            request.setRole(com.medical.clinic.enums.Role.PATIENT);
        }
        UserResponse user = userService.registerWalkInPatient(request);
        return ResponseEntity.ok(new ApiResponse<>("Walk-in patient registered", false, true, user));
    }

    @GetMapping("/patients")
    @Operation(summary = "View all patient records")
    public ResponseEntity<ApiResponse<List<Patient>>> patients() {
        return ResponseEntity.ok(new ApiResponse<>("Patients fetched", false, true, patientService.getAllPatients()));
    }

    @GetMapping("/patients/{id}")
    @Operation(summary = "Get patient record by id")
    public ResponseEntity<ApiResponse<Patient>> getPatient(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>("Patient fetched", false, true, patientService.getPatientById(id)));
    }

    @PutMapping("/patients/{id}")
    @Operation(summary = "Update patient medical record")
    public ResponseEntity<ApiResponse<Patient>> updatePatient(
            @PathVariable String id,
            @RequestBody Patient patient
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Patient updated", false, true, patientService.updatePatient(id, patient)));
    }

    @GetMapping("/doctors")
    @Operation(summary = "View doctors and schedules")
    public ResponseEntity<ApiResponse<List<DoctorResponse>>> doctors() {
        return ResponseEntity.ok(new ApiResponse<>("Doctors fetched", false, true, doctorService.getActiveDoctors()));
    }

    @GetMapping("/doctors/{id}/schedule")
    @Operation(summary = "View doctor appointments")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> doctorSchedule(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Doctor schedule fetched",
                false,
                true,
                appointmentService.getAppointmentsByDoctorId(id)
        ));
    }

    @PostMapping("/appointments")
    @Operation(summary = "Create appointment for walk-in or registered patient")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody AppointmentCreateRequest request
    ) {
        AppointmentResponse response = appointmentService.createAppointment(
                request,
                SecurityUtils.currentUserEmail()
        );
        return ResponseEntity.ok(new ApiResponse<>("Appointment created", false, true, response));
    }

    @GetMapping("/appointments/today")
    @Operation(summary = "Today's appointments overview")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> todayAppointments() {
        List<AppointmentResponse> all = appointmentService.getAllAppointments();
        java.time.LocalDate today = java.time.LocalDate.now();
        List<AppointmentResponse> todayList = all.stream()
                .filter(a -> today.equals(a.getAppointmentDate()))
                .toList();
        return ResponseEntity.ok(new ApiResponse<>("Today's appointments", false, true, todayList));
    }

    @GetMapping("/appointments")
    @Operation(summary = "All appointments for reception desk")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> allAppointments() {
        return ResponseEntity.ok(new ApiResponse<>(
                "Appointments fetched", false, true, appointmentService.getAllAppointments()));
    }
}
