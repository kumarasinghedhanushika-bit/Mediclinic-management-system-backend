package com.medical.clinic.scheduler;

import com.medical.clinic.service.AppointmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AppointmentReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(AppointmentReminderScheduler.class);

    private final AppointmentService appointmentService;

    public AppointmentReminderScheduler(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /** Runs every 5 minutes; sends email reminders ~1 hour before appointment. */
    @Scheduled(cron = "${app.reminder.cron:0 */5 * * * *}")
    public void sendReminders() {
        try {
            appointmentService.sendUpcomingReminders();
        } catch (Exception e) {
            log.error("Failed to send appointment reminders", e);
        }
    }
}
