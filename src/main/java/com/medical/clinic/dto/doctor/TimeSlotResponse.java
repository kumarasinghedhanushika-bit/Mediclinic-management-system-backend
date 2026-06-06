package com.medical.clinic.dto.doctor;

import java.time.LocalTime;

public class TimeSlotResponse {

    private LocalTime time;
    private boolean available;

    public TimeSlotResponse() {
    }

    public TimeSlotResponse(LocalTime time, boolean available) {
        this.time = time;
        this.available = available;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
