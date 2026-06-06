package com.medical.clinic.service;

import com.medical.clinic.Emails.emailTemplates;
import com.medical.clinic.model.Appointment;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class AppointmentNotificationService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");

    private final EmailServise emailService;

    public AppointmentNotificationService(EmailServise emailService) {
        this.emailService = emailService;
    }

    public void sendBookingConfirmation(Appointment appointment) {
        if (appointment.getPatientEmail() == null || appointment.getPatientEmail().isBlank()) {
            return;
        }
        emailService.sendEmail(
                appointment.getPatientEmail(),
                "Appointment Confirmed - " + appointment.getAppointmentNumber(),
                emailTemplates.appointmentConfirmationEmail(
                        appointment.getPatientName(),
                        appointment.getAppointmentNumber(),
                        appointment.getDoctorName(),
                        appointment.getDepartmentName(),
                        appointment.getAppointmentDate().format(DATE_FMT),
                        appointment.getAppointmentTime().format(TIME_FMT),
                        appointment.getStatus().name(),
                        appointment.getConsultationFee()
                )
        );
    }

    public void sendReminder(Appointment appointment) {
        if (appointment.getPatientEmail() == null || appointment.getPatientEmail().isBlank()) {
            return;
        }
        emailService.sendEmail(
                appointment.getPatientEmail(),
                "Reminder: Appointment in 1 Hour - " + appointment.getAppointmentNumber(),
                emailTemplates.appointmentReminderEmail(
                        appointment.getPatientName(),
                        appointment.getAppointmentNumber(),
                        appointment.getDoctorName(),
                        appointment.getAppointmentDate().format(DATE_FMT),
                        appointment.getAppointmentTime().format(TIME_FMT)
                )
        );
    }

    public void sendCancellation(Appointment appointment) {
        if (appointment.getPatientEmail() == null || appointment.getPatientEmail().isBlank()) {
            return;
        }
        emailService.sendEmail(
                appointment.getPatientEmail(),
                "Appointment Cancelled - " + appointment.getAppointmentNumber(),
                emailTemplates.appointmentCancellationEmail(
                        appointment.getPatientName(),
                        appointment.getAppointmentNumber(),
                        appointment.getDoctorName(),
                        appointment.getAppointmentDate().format(DATE_FMT),
                        appointment.getAppointmentTime().format(TIME_FMT)
                )
        );
    }

    public void sendReschedule(Appointment appointment) {
        if (appointment.getPatientEmail() == null || appointment.getPatientEmail().isBlank()) {
            return;
        }
        emailService.sendEmail(
                appointment.getPatientEmail(),
                "Appointment Rescheduled - " + appointment.getAppointmentNumber(),
                emailTemplates.appointmentRescheduleEmail(
                        appointment.getPatientName(),
                        appointment.getAppointmentNumber(),
                        appointment.getDoctorName(),
                        appointment.getAppointmentDate().format(DATE_FMT),
                        appointment.getAppointmentTime().format(TIME_FMT)
                )
        );
    }
}
