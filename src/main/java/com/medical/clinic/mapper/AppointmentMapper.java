package com.medical.clinic.mapper;

import com.medical.clinic.dto.appointment.AppointmentResponse;
import com.medical.clinic.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {
        AppointmentResponse response = new AppointmentResponse();
        response.setId(appointment.getId());
        response.setAppointmentNumber(appointment.getAppointmentNumber());
        response.setPatientId(appointment.getPatientId());
        response.setPatientName(appointment.getPatientName());
        response.setPatientEmail(appointment.getPatientEmail());
        response.setPatientPhone(appointment.getPatientPhone());
        response.setDoctorId(appointment.getDoctorId());
        response.setDoctorName(appointment.getDoctorName());
        response.setDepartmentId(appointment.getDepartmentId());
        response.setDepartmentName(appointment.getDepartmentName());
        response.setAppointmentDate(appointment.getAppointmentDate());
        response.setAppointmentTime(appointment.getAppointmentTime());
        response.setAppointmentDateTime(appointment.getAppointmentDateTime());
        response.setReason(appointment.getReason());
        response.setStatus(appointment.getStatus());
        response.setNotes(appointment.getNotes());
        response.setConsultationFee(appointment.getConsultationFee());
        response.setCreatedAt(appointment.getCreatedAt());
        return response;
    }
}
