package com.medical.clinic.service;

import com.medical.clinic.dto.doctor.TimeSlotResponse;
import com.medical.clinic.enums.AppointmentStatus;
import com.medical.clinic.model.Appointment;
import com.medical.clinic.model.Doctor;
import com.medical.clinic.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorSchedulingService {

    private static final Set<AppointmentStatus> BLOCKING_STATUSES = EnumSet.of(
            AppointmentStatus.PENDING,
            AppointmentStatus.CONFIRMED
    );

    private final AppointmentRepository appointmentRepository;

    public DoctorSchedulingService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<TimeSlotResponse> getAvailableSlots(Doctor doctor, LocalDate date) {
        validateDoctorWorksOnDate(doctor, date);

        LocalTime start = LocalTime.parse(doctor.getConsultationStartTime());
        LocalTime end = LocalTime.parse(doctor.getConsultationEndTime());
        int slotMinutes = doctor.getSlotDurationMinutes() != null ? doctor.getSlotDurationMinutes() : 30;

        Set<LocalTime> bookedTimes = appointmentRepository
                .findByDoctorIdAndAppointmentDateAndStatusIn(
                        doctor.getId(),
                        date,
                        BLOCKING_STATUSES
                )
                .stream()
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toSet());

        List<TimeSlotResponse> slots = new ArrayList<>();
        LocalTime current = start;
        LocalTime nowTime = LocalTime.now();
        LocalDate today = LocalDate.now();

        while (!current.isAfter(end.minusMinutes(slotMinutes))) {
            boolean pastSlot = date.equals(today) && current.isBefore(nowTime);
            boolean booked = bookedTimes.contains(current);
            slots.add(new TimeSlotResponse(current, !pastSlot && !booked));
            current = current.plusMinutes(slotMinutes);
        }

        return slots;
    }

    public void validateSlotAvailable(Doctor doctor, LocalDate date, LocalTime time) {
        validateDoctorWorksOnDate(doctor, date);

        boolean exists = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusIn(
                doctor.getId(),
                date,
                time,
                BLOCKING_STATUSES
        );
        if (exists) {
            throw new RuntimeException("Selected time slot is already booked");
        }

        List<TimeSlotResponse> slots = getAvailableSlots(doctor, date);
        boolean available = slots.stream()
                .anyMatch(s -> s.getTime().equals(time) && s.isAvailable());
        if (!available) {
            throw new RuntimeException("Selected time slot is not available");
        }
    }

    private void validateDoctorWorksOnDate(Doctor doctor, LocalDate date) {
        if (doctor.getAvailableDays() == null || doctor.getAvailableDays().isEmpty()) {
            throw new RuntimeException("Doctor has no available days configured");
        }
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase();
        String shortDay = date.getDayOfWeek().name();

        boolean works = doctor.getAvailableDays().stream()
                .map(String::toUpperCase)
                .anyMatch(d -> d.equals(dayName) || d.equals(shortDay)
                        || d.startsWith(dayName.substring(0, 3))
                        || d.equals(date.getDayOfWeek().name().substring(0, 3)));

        if (!works) {
            throw new RuntimeException("Doctor is not available on " + dayName);
        }
    }
}
