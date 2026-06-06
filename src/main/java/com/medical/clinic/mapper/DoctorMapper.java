package com.medical.clinic.mapper;

import com.medical.clinic.dto.doctor.DoctorRequest;
import com.medical.clinic.dto.doctor.DoctorResponse;
import com.medical.clinic.model.Department;
import com.medical.clinic.model.Doctor;
import com.medical.clinic.model.User;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public Doctor toEntity(DoctorRequest request) {
        Doctor doctor = new Doctor();
        doctor.setUserId(request.getUserId());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setDepartmentId(request.getDepartmentId());
        doctor.setLicenseNumber(request.getLicenseNumber());
        doctor.setExperienceYears(request.getExperienceYears());
        doctor.setAvailableDays(request.getAvailableDays());
        doctor.setConsultationStartTime(request.getConsultationStartTime());
        doctor.setConsultationEndTime(request.getConsultationEndTime());
        doctor.setSlotDurationMinutes(request.getSlotDurationMinutes() != null
                ? request.getSlotDurationMinutes() : 30);
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setActive(request.getActive() != null ? request.getActive() : true);
        return doctor;
    }

    public DoctorResponse toResponse(Doctor doctor, User user, Department department) {
        DoctorResponse response = new DoctorResponse();
        response.setId(doctor.getId());
        response.setUserId(doctor.getUserId());
        if (user != null) {
            response.setDoctorName(user.getFirstName() + " " + user.getLastName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());
        }
        response.setSpecialization(doctor.getSpecialization());
        response.setDepartmentId(doctor.getDepartmentId());
        if (department != null) {
            response.setDepartmentName(department.getName());
        }
        response.setLicenseNumber(doctor.getLicenseNumber());
        response.setExperienceYears(doctor.getExperienceYears());
        response.setAvailableDays(doctor.getAvailableDays());
        response.setConsultationStartTime(doctor.getConsultationStartTime());
        response.setConsultationEndTime(doctor.getConsultationEndTime());
        response.setSlotDurationMinutes(doctor.getSlotDurationMinutes());
        response.setConsultationFee(doctor.getConsultationFee());
        response.setActive(doctor.getActive());
        return response;
    }
}
