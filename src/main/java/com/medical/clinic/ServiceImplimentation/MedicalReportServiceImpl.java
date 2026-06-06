package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.dto.report.MedicalReportRequest;
import com.medical.clinic.dto.report.MedicalReportResponse;
import com.medical.clinic.enums.CloudinaryFolder;
import com.medical.clinic.enums.ReportType;
import com.medical.clinic.mapper.MedicalReportMapper;
import com.medical.clinic.model.MedicalReport;
import com.medical.clinic.repository.MedicalReportRepository;
import com.medical.clinic.service.CloudinaryService;
import com.medical.clinic.service.MedicalReportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicalReportServiceImpl implements MedicalReportService {

    private final MedicalReportRepository reportRepository;
    private final MedicalReportMapper reportMapper;
    private final CloudinaryService cloudinaryService;

    public MedicalReportServiceImpl(
            MedicalReportRepository reportRepository,
            MedicalReportMapper reportMapper,
            CloudinaryService cloudinaryService
    ) {
        this.reportRepository = reportRepository;
        this.reportMapper = reportMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public MedicalReportResponse createReport(
            MedicalReportRequest request,
            MultipartFile file,
            String uploaderEmail
    ) {
        MedicalReport report = reportMapper.toEntity(request);
        if (file != null && !file.isEmpty()) {
            CloudinaryFolder folder = request.getReportType() == ReportType.LAB
                    || request.getReportType() == ReportType.PATHOLOGY
                    ? CloudinaryFolder.LAB_REPORTS
                    : CloudinaryFolder.REPORTS;
            report.setReportFileUrl(cloudinaryService.uploadFile(file, folder));
        }
        return reportMapper.toResponse(reportRepository.save(report));
    }

    @Override
    public MedicalReportResponse updateReport(
            String id,
            MedicalReportRequest request,
            MultipartFile file,
            String uploaderEmail
    ) {
        MedicalReport report = getEntity(id);
        report.setPatientId(request.getPatientId());
        report.setDoctorId(request.getDoctorId());
        report.setLabTechnicianId(request.getLabTechnicianId());
        report.setReportType(request.getReportType());
        report.setDiagnosis(request.getDiagnosis());
        report.setNotes(request.getNotes());
        report.setUpdatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            if (report.getReportFileUrl() != null) {
                cloudinaryService.deleteFile(report.getReportFileUrl());
            }
            CloudinaryFolder folder = request.getReportType() == ReportType.LAB
                    || request.getReportType() == ReportType.PATHOLOGY
                    ? CloudinaryFolder.LAB_REPORTS
                    : CloudinaryFolder.REPORTS;
            report.setReportFileUrl(cloudinaryService.uploadFile(file, folder));
        }

        return reportMapper.toResponse(reportRepository.save(report));
    }

    @Override
    public MedicalReportResponse getById(String id) {
        return reportMapper.toResponse(getEntity(id));
    }

    @Override
    public List<MedicalReportResponse> getByPatientId(String patientId) {
        return reportRepository.findByPatientId(patientId).stream()
                .map(reportMapper::toResponse)
                .toList();
    }

    @Override
    public List<MedicalReportResponse> getAll() {
        return reportRepository.findAll().stream()
                .map(reportMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteReport(String id) {
        MedicalReport report = getEntity(id);
        if (report.getReportFileUrl() != null) {
            cloudinaryService.deleteFile(report.getReportFileUrl());
        }
        reportRepository.deleteById(id);
    }

    @Override
    public byte[] downloadReportFile(String id) {
        MedicalReport report = getEntity(id);
        if (report.getReportFileUrl() == null) {
            throw new RuntimeException("No file attached to this report");
        }
        try {
            return java.net.URI.create(report.getReportFileUrl()).toURL().openStream().readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to download report file");
        }
    }

    private MedicalReport getEntity(String id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical report not found"));
    }
}
