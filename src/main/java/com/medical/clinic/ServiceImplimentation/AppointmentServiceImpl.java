package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.dto.appointment.*;
import com.medical.clinic.enums.AppointmentStatus;
import com.medical.clinic.enums.Role;
import com.medical.clinic.mapper.AppointmentMapper;
import com.medical.clinic.model.*;
import com.medical.clinic.repository.AppointmentRepository;
import com.medical.clinic.repository.DepartmentRepository;
import com.medical.clinic.repository.DoctorRepository;
import com.medical.clinic.repository.UserRepository;
import com.medical.clinic.service.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final Set<AppointmentStatus> REMINDER_STATUSES = EnumSet.of(
            AppointmentStatus.PENDING,
            AppointmentStatus.CONFIRMED
    );

    private static final Set<AppointmentStatus> CANCELLABLE = EnumSet.of(
            AppointmentStatus.PENDING,
            AppointmentStatus.CONFIRMED
    );

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final PatientService patientService;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final DoctorSchedulingService schedulingService;
    private final AppointmentMapper appointmentMapper;
    private final AppointmentNotificationService notificationService;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            PatientService patientService,
            DoctorRepository doctorRepository,
            DepartmentRepository departmentRepository,
            DoctorSchedulingService schedulingService,
            AppointmentMapper appointmentMapper,
            AppointmentNotificationService notificationService
    ) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
        this.departmentRepository = departmentRepository;
        this.schedulingService = schedulingService;
        this.appointmentMapper = appointmentMapper;
        this.notificationService = notificationService;
    }

    @Override
    public AppointmentResponse bookAppointment(AppointmentBookRequest request, String patientUserEmail) {
        User user = userRepository.findByEmail(patientUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() != Role.PATIENT) {
            throw new RuntimeException("Only patients can book appointments online");
        }
        Patient patient = patientService.getOrCreateForUser(user);
        return createAndSave(
                patient,
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                request.getReason(),
                request.getNotes(),
                user.getId(),
                user.getRole().name(),
                AppointmentStatus.PENDING
        );
    }

    @Override
    public AppointmentResponse createAppointment(AppointmentCreateRequest request, String bookedByEmail) {
        User actor = userRepository.findByEmail(bookedByEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = patientService.getPatientById(request.getPatientId());
        User patientUser = userRepository.findById(patient.getUserId()).orElse(null);

        AppointmentResponse response = createAndSave(
                patient,
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                request.getReason(),
                request.getNotes(),
                actor.getId(),
                actor.getRole().name(),
                AppointmentStatus.CONFIRMED
        );

        return response;
    }

    @Override
    public AppointmentResponse confirmAppointment(String id, String actorEmail) {
        Appointment appointment = getEntity(id);
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new RuntimeException("Only pending appointments can be confirmed");
        }
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment saved = appointmentRepository.save(appointment);
        notificationService.sendBookingConfirmation(saved);
        return appointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse cancelAppointment(String id, String actorEmail) {
        Appointment appointment = getEntity(id);
        User actor = userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertCanModify(appointment, actor);

        if (!CANCELLABLE.contains(appointment.getStatus())) {
            throw new RuntimeException("Appointment cannot be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setReminderSent(true);
        Appointment saved = appointmentRepository.save(appointment);
        notificationService.sendCancellation(saved);
        return appointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse rescheduleAppointment(
            String id,
            AppointmentRescheduleRequest request,
            String actorEmail
    ) {
        Appointment appointment = getEntity(id);
        User actor = userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertCanModify(appointment, actor);

        if (!CANCELLABLE.contains(appointment.getStatus())) {
            throw new RuntimeException("Appointment cannot be rescheduled");
        }

        Doctor doctor = getDoctor(appointment.getDoctorId());
        validateDateTime(request.getAppointmentDate(), request.getAppointmentTime());
        schedulingService.validateSlotAvailable(doctor, request.getAppointmentDate(), request.getAppointmentTime());

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentDateTime(LocalDateTime.of(
                request.getAppointmentDate(),
                request.getAppointmentTime()
        ));
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        appointment.setReminderSent(false);
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment saved = appointmentRepository.save(appointment);
        notificationService.sendReschedule(saved);
        return appointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse updateStatus(
            String id,
            AppointmentStatusUpdateRequest request,
            String doctorUserEmail
    ) {
        User doctorUser = userRepository.findByEmail(doctorUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = doctorRepository.findByUserId(doctorUser.getId())
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        Appointment appointment = getEntity(id);
        if (!appointment.getDoctorId().equals(doctor.getId())) {
            throw new RuntimeException("You can only update your own appointments");
        }

        appointment.setStatus(request.getStatus());
        if (request.getNotes() != null) {
            appointment.setNotes(request.getNotes());
        }
        if (request.getStatus() == AppointmentStatus.COMPLETED
                || request.getStatus() == AppointmentStatus.NO_SHOW
                || request.getStatus() == AppointmentStatus.CANCELLED) {
            appointment.setReminderSent(true);
        }

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse updateAppointment(String id, AppointmentCreateRequest request, String actorEmail) {
        Appointment appointment = getEntity(id);
        User actor = userRepository.findByEmail(actorEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (actor.getRole() != Role.RECEPTIONIST && actor.getRole() != Role.ADMIN) {
            throw new RuntimeException("Not authorized to edit appointments");
        }

        Doctor doctor = getDoctor(request.getDoctorId());
        schedulingService.validateSlotAvailable(doctor, request.getAppointmentDate(), request.getAppointmentTime());

        Patient patient = patientService.getPatientById(request.getPatientId());
        populatePatientAndDoctor(appointment, patient, doctor);

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentDateTime(LocalDateTime.of(
                request.getAppointmentDate(),
                request.getAppointmentTime()
        ));
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public AppointmentResponse getAppointmentById(String id) {
        return appointmentMapper.toResponse(getEntity(id));
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getMyPatientAppointments(String patientUserEmail) {
        User user = userRepository.findByEmail(patientUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return appointmentRepository.findByPatientUserId(user.getId()).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getMyDoctorAppointments(String doctorUserEmail) {
        User user = userRepository.findByEmail(doctorUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return appointmentRepository.findByDoctorUserId(user.getId()).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByPatientId(String patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public void sendUpcomingReminders() {
        LocalDateTime windowStart = LocalDateTime.now().plusMinutes(55);
        LocalDateTime windowEnd = LocalDateTime.now().plusMinutes(65);

        List<Appointment> due = appointmentRepository
                .findByReminderSentFalseAndStatusInAndAppointmentDateTimeBetween(
                        REMINDER_STATUSES,
                        windowStart,
                        windowEnd
                );

        for (Appointment appointment : due) {
            notificationService.sendReminder(appointment);
            appointment.setReminderSent(true);
            appointmentRepository.save(appointment);
        }
    }

    private AppointmentResponse createAndSave(
            Patient patient,
            String doctorId,
            LocalDate date,
            LocalTime time,
            String reason,
            String notes,
            String bookedByUserId,
            String bookedByRole,
            AppointmentStatus initialStatus
    ) {
        Doctor doctor = getDoctor(doctorId);
        if (!Boolean.TRUE.equals(doctor.getActive())) {
            throw new RuntimeException("Doctor is not available for channeling");
        }
        validateDateTime(date, time);
        schedulingService.validateSlotAvailable(doctor, date, time);

        User patientUser = userRepository.findById(patient.getUserId()).orElse(null);

        Appointment appointment = Appointment.builder()
                .appointmentNumber("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .patientId(patient.getId())
                .reason(reason)
                .notes(notes)
                .appointmentDate(date)
                .appointmentTime(time)
                .appointmentDateTime(LocalDateTime.of(date, time))
                .status(initialStatus)
                .consultationFee(doctor.getConsultationFee())
                .bookedByUserId(bookedByUserId)
                .bookedByRole(bookedByRole)
                .reminderSent(false)
                .build();

        populatePatientAndDoctor(appointment, patient, doctor);
        if (patientUser != null) {
            appointment.setPatientUserId(patientUser.getId());
            appointment.setPatientName(patientUser.getFirstName() + " " + patientUser.getLastName());
            appointment.setPatientEmail(patientUser.getEmail());
            appointment.setPatientPhone(patientUser.getPhone());
        }

        Appointment saved = appointmentRepository.save(appointment);
        notificationService.sendBookingConfirmation(saved);
        return appointmentMapper.toResponse(saved);
    }

    private void populatePatientAndDoctor(Appointment appointment, Patient patient, Doctor doctor) {
        appointment.setPatientId(patient.getId());
        User patientUser = userRepository.findById(patient.getUserId()).orElse(null);
        if (patientUser != null) {
            appointment.setPatientUserId(patientUser.getId());
            appointment.setPatientName(patientUser.getFirstName() + " " + patientUser.getLastName());
            appointment.setPatientEmail(patientUser.getEmail());
            appointment.setPatientPhone(patientUser.getPhone());
        }
        appointment.setDoctorId(doctor.getId());
        appointment.setDoctorUserId(doctor.getUserId());
        User doctorUser = userRepository.findById(doctor.getUserId()).orElse(null);
        if (doctorUser != null) {
            appointment.setDoctorName(doctorUser.getFirstName() + " " + doctorUser.getLastName());
        }
        appointment.setDepartmentId(doctor.getDepartmentId());
        departmentRepository.findById(doctor.getDepartmentId())
                .ifPresent(d -> appointment.setDepartmentName(d.getName()));
        appointment.setConsultationFee(doctor.getConsultationFee());
    }

    private Doctor getDoctor(String doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    private void assertCanModify(Appointment appointment, User actor) {
        if (actor.getRole() == Role.ADMIN || actor.getRole() == Role.RECEPTIONIST) {
            return;
        }
        if (actor.getRole() == Role.PATIENT
                && appointment.getPatientUserId() != null
                && appointment.getPatientUserId().equals(actor.getId())) {
            return;
        }
        throw new RuntimeException("Not authorized to modify this appointment");
    }

    private Appointment getEntity(String id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    private void validateDateTime(LocalDate date, LocalTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new RuntimeException("Appointment date cannot be in the past");
        }
        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new RuntimeException("Appointment time cannot be in the past");
        }
    }
}
