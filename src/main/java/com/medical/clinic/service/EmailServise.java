package com.medical.clinic.service;


public interface EmailServise {
    void sendEmail(String to, String subject, String message);
}