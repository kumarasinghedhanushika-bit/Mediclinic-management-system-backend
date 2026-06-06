package com.medical.clinic.service;

import com.medical.clinic.dto.doctor.DoctorRequest;
import com.medical.clinic.dto.doctor.DoctorResponse;
import com.medical.clinic.dto.doctor.TimeSlotResponse;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {

    List<DoctorResponse> getAllDoctors();

    List<DoctorResponse> getActiveDoctors();

    List<DoctorResponse> getDoctorsByDepartment(String departmentId);

    DoctorResponse getDoctorById(String id);

    DoctorResponse getDoctorByUserId(String userId);

    DoctorResponse createDoctor(DoctorRequest request);

    DoctorResponse updateDoctor(String id, DoctorRequest request);

    void deleteDoctor(String id);

    List<TimeSlotResponse> getAvailableSlots(String doctorId, LocalDate date);
}
