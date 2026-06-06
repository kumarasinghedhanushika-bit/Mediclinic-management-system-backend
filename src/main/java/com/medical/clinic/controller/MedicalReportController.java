package com.medical.clinic.controller;

import com.medical.clinic.dto.report.MedicalReportRequest;
import com.medical.clinic.dto.report.MedicalReportResponse;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.security.SecurityUtils;
import com.medical.clinic.service.MedicalReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/medical-reports")
@CrossOrigin("*")
@Tag(name = "Medical Reports", description = "Upload and manage medical/lab reports")
public class MedicalReportController {

    private final MedicalReportService medicalReportService;

    public MedicalReportController(MedicalReportService medicalReportService) {
        this.medicalReportService = medicalReportService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN')")
    @Operation(summary = "Upload medical report with file")
    public ResponseEntity<ApiResponse<MedicalReportResponse>> create(
            @RequestPart("report") String reportJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        MedicalReportRequest request = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(reportJson, MedicalReportRequest.class);
        MedicalReportResponse response = medicalReportService.createReport(
                request, file, SecurityUtils.currentUserEmail());
        return ResponseEntity.ok(new ApiResponse<>("Report created", false, true, response));
    }

    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<MedicalReportResponse>> createJson(
            @RequestBody MedicalReportRequest request
    ) {
        MedicalReportResponse response = medicalReportService.createReport(
                request, null, SecurityUtils.currentUserEmail());
        return ResponseEntity.ok(new ApiResponse<>("Report created", false, true, response));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<MedicalReportResponse>> update(
            @PathVariable String id,
            @RequestPart("report") String reportJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        MedicalReportRequest request = new com.fasterxml.jackson.databind.ObjectMapper()
                .readValue(reportJson, MedicalReportRequest.class);
        MedicalReportResponse response = medicalReportService.updateReport(
                id, request, file, SecurityUtils.currentUserEmail());
        return ResponseEntity.ok(new ApiResponse<>("Report updated", false, true, response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN', 'PATIENT', 'NURSE')")
    public ResponseEntity<ApiResponse<MedicalReportResponse>> getById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Report fetched", false, true, medicalReportService.getById(id)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN', 'PATIENT', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<MedicalReportResponse>>> byPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Reports fetched", false, true, medicalReportService.getByPatientId(patientId)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<List<MedicalReportResponse>>> getAll() {
        return ResponseEntity.ok(new ApiResponse<>(
                "Reports fetched", false, true, medicalReportService.getAll()));
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN', 'PATIENT')")
    @Operation(summary = "Download report file")
    public ResponseEntity<byte[]> download(@PathVariable String id) {
        medicalReportService.getById(id);
        byte[] data = medicalReportService.downloadReportFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report-" + id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        medicalReportService.deleteReport(id);
        return ResponseEntity.ok(new ApiResponse<>("Report deleted", false, true, null));
    }
}
