package com.medical.clinic.controller;

import com.medical.clinic.enums.CloudinaryFolder;
import com.medical.clinic.model.ApiResponse;
import com.medical.clinic.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
@Tag(name = "File Upload", description = "Cloudinary file upload and delete")
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload profile image (max 5MB, jpeg/png/webp)")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadProfile(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadImage(file, CloudinaryFolder.PROFILE_IMAGES);
        return ResponseEntity.ok(new ApiResponse<>("Uploaded", false, true, Map.of("url", url)));
    }

    @PostMapping("/upload/report")
    @PreAuthorize("hasAnyRole('DOCTOR', 'LAB_TECHNICIAN', 'ADMIN')")
    @Operation(summary = "Upload medical report file (image or PDF)")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadReport(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file, CloudinaryFolder.REPORTS);
        return ResponseEntity.ok(new ApiResponse<>("Uploaded", false, true, Map.of("url", url)));
    }

    @PostMapping("/upload/lab-report")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'DOCTOR', 'ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadLabReport(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file, CloudinaryFolder.LAB_REPORTS);
        return ResponseEntity.ok(new ApiResponse<>("Uploaded", false, true, Map.of("url", url)));
    }

    @PostMapping("/upload/prescription")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PHARMACIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadPrescription(@RequestParam("file") MultipartFile file) {
        String url = cloudinaryService.uploadFile(file, CloudinaryFolder.PRESCRIPTIONS);
        return ResponseEntity.ok(new ApiResponse<>("Uploaded", false, true, Map.of("url", url)));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete file from Cloudinary by URL")
    public ResponseEntity<ApiResponse<Void>> delete(@RequestParam String url) {
        cloudinaryService.deleteFile(url);
        return ResponseEntity.ok(new ApiResponse<>("File deleted", false, true, null));
    }
}
