package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.dto.doctor.DoctorRequest;
import com.medical.clinic.dto.doctor.DoctorResponse;
import com.medical.clinic.dto.doctor.TimeSlotResponse;
import com.medical.clinic.enums.Role;
import com.medical.clinic.mapper.DoctorMapper;
import com.medical.clinic.model.Department;
import com.medical.clinic.model.Doctor;
import com.medical.clinic.model.User;
import com.medical.clinic.repository.DepartmentRepository;
import com.medical.clinic.repository.DoctorRepository;
import com.medical.clinic.repository.UserRepository;
import com.medical.clinic.service.DoctorSchedulingService;
import com.medical.clinic.service.DoctorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorMapper doctorMapper;
    private final DoctorSchedulingService schedulingService;

    public DoctorServiceImpl(
            DoctorRepository doctorRepository,
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            DoctorMapper doctorMapper,
            DoctorSchedulingService schedulingService
    ) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.doctorMapper = doctorMapper;
        this.schedulingService = schedulingService;
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<DoctorResponse> getActiveDoctors() {
        return doctorRepository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<DoctorResponse> getDoctorsByDepartment(String departmentId) {
        return doctorRepository.findByDepartmentId(departmentId).stream()
                .filter(d -> Boolean.TRUE.equals(d.getActive()))
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DoctorResponse getDoctorById(String id) {
        return toResponse(getDoctorEntity(id));
    }

    @Override
    public DoctorResponse getDoctorByUserId(String userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));
        return toResponse(doctor);
    }

    @Override
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (Boolean.TRUE.equals(doctorRepository.existsByLicenseNumber(request.getLicenseNumber()))) {
            throw new RuntimeException("License number already exists");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.DOCTOR) {
            throw new RuntimeException("User must have DOCTOR role");
        }
        if (doctorRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new RuntimeException("Doctor profile already exists for this user");
        }
        departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Doctor doctor = doctorMapper.toEntity(request);
        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    public DoctorResponse updateDoctor(String id, DoctorRequest request) {
        Doctor existing = getDoctorEntity(id);
        existing.setSpecialization(request.getSpecialization());
        existing.setDepartmentId(request.getDepartmentId());
        existing.setLicenseNumber(request.getLicenseNumber());
        existing.setExperienceYears(request.getExperienceYears());
        existing.setAvailableDays(request.getAvailableDays());
        existing.setConsultationStartTime(request.getConsultationStartTime());
        existing.setConsultationEndTime(request.getConsultationEndTime());
        if (request.getSlotDurationMinutes() != null) {
            existing.setSlotDurationMinutes(request.getSlotDurationMinutes());
        }
        existing.setConsultationFee(request.getConsultationFee());
        if (request.getActive() != null) {
            existing.setActive(request.getActive());
        }
        return toResponse(doctorRepository.save(existing));
    }

    @Override
    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public List<TimeSlotResponse> getAvailableSlots(String doctorId, LocalDate date) {
        Doctor doctor = getDoctorEntity(doctorId);
        if (!Boolean.TRUE.equals(doctor.getActive())) {
            throw new RuntimeException("Doctor is not active");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot view slots for past dates");
        }
        return schedulingService.getAvailableSlots(doctor, date);
    }

    public Doctor getDoctorEntity(String id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    private DoctorResponse toResponse(Doctor doctor) {
        User user = userRepository.findById(doctor.getUserId()).orElse(null);
        Department department = departmentRepository.findById(doctor.getDepartmentId()).orElse(null);
        return doctorMapper.toResponse(doctor, user, department);
    }
}
