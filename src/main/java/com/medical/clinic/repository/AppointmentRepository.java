package com.medical.clinic.repository;

import com.medical.clinic.enums.AppointmentStatus;
import com.medical.clinic.model.Appointment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends MongoRepository<Appointment, String> {

    List<Appointment> findByPatientId(String patientId);

    List<Appointment> findByPatientUserId(String patientUserId);

    List<Appointment> findByDoctorId(String doctorId);

    List<Appointment> findByDoctorUserId(String doctorUserId);

    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
            String doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Collection<AppointmentStatus> statuses
    );

    List<Appointment> findByDoctorIdAndAppointmentDateAndStatusIn(
            String doctorId,
            LocalDate appointmentDate,
            Collection<AppointmentStatus> statuses
    );

    List<Appointment> findByReminderSentFalseAndStatusInAndAppointmentDateTimeBetween(
            Collection<AppointmentStatus> statuses,
            LocalDateTime start,
            LocalDateTime end
    );

}
