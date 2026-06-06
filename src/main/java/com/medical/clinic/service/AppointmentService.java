package com.medical.clinic.service;

import com.medical.clinic.dto.appointment.*;
import com.medical.clinic.enums.AppointmentStatus;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse bookAppointment(AppointmentBookRequest request, String patientUserEmail);

    AppointmentResponse createAppointment(AppointmentCreateRequest request, String bookedByEmail);

    AppointmentResponse confirmAppointment(String id, String actorEmail);

    AppointmentResponse cancelAppointment(String id, String actorEmail);

    AppointmentResponse rescheduleAppointment(String id, AppointmentRescheduleRequest request, String actorEmail);

    AppointmentResponse updateStatus(String id, AppointmentStatusUpdateRequest request, String doctorUserEmail);

    AppointmentResponse updateAppointment(String id, AppointmentCreateRequest request, String actorEmail);

    AppointmentResponse getAppointmentById(String id);

    List<AppointmentResponse> getAllAppointments();

    List<AppointmentResponse> getMyPatientAppointments(String patientUserEmail);

    List<AppointmentResponse> getMyDoctorAppointments(String doctorUserEmail);

    List<AppointmentResponse> getAppointmentsByPatientId(String patientId);

    List<AppointmentResponse> getAppointmentsByDoctorId(String doctorId);

    void sendUpcomingReminders();
}
