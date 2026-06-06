package com.medical.clinic.Emails;
import com.medical.clinic.model.User;

import java.time.Year;

public class emailTemplates {
    // ================= HEADER =================
    public static String header() {
        return """
                <div style="
                    background: linear-gradient(135deg, #0d6efd, #6610f2);
                    color: #fff;
                    text-align: center;
                    padding: 28px 20px;
                    font-size: 26px;
                    font-weight: bold;
                    letter-spacing: 1px;
                ">
                    🎫 Clinic Management System
                    <div style="font-size:14px; font-weight:400; margin-top:5px;">
                        Fast • Secure • Reliable Booking Platform
                    </div>
                </div>
                """;
    }

    // ================= FOOTER =================
    public static String footer() {
        return """
                    <div style="
                        background-color: #f8f9fa;
                        color: #6c757d;
                        text-align: center;
                        padding: 20px;
                        font-size: 13px;
                        border-top: 1px solid #e9ecef;
                    ">
                
                        <p style="margin:5px 0;">
                            © %s Clinic Management System. All rights reserved.
                        </p>
                
                        <p style="margin:5px 0;">
                            📍 Sri Lanka | 🌐 www.clinic.com
                        </p>
                
                        <p style="margin:5px 0; font-size:12px;">
                            This is an automated email. Please do not reply.
                        </p>
                
                    </div>
                """.formatted(Year.now().getValue());
    }

    // ================= OTP EMAIL =================
    public static String otpEmail(String name, String otp) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>OTP Verification</title>
                </head>
                <body style="margin:0; padding:0; font-family:Arial; background:#f4f4f7;">
                
                    <div style="max-width:600px; margin:40px auto; background:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1);">
                
                        %s
                
                        <div style="padding:30px 20px; text-align:center; color:#333;">
                
                            <h1 style="color:#0d6efd;">OTP Verification</h1>
                
                            <p>Hello <b>%s</b>,</p>
                
                            <p>Use the following OTP to verify your account:</p>
                
                            <div style="
                                font-size:28px;
                                font-weight:bold;
                                letter-spacing:5px;
                                color:#6610f2;
                                margin:20px 0;
                                padding:10px;
                                background:#f1f1f1;
                                display:inline-block;
                                border-radius:8px;
                            ">
                                %s
                            </div>
                
                            <p>This OTP is valid for a short time only.</p>
                
                            <p style="margin-top:20px; color:#888;">
                                If you didn’t request this, please ignore this email.
                            </p>
                
                        </div>
                
                        %s
                
                    </div>
                
                </body>
                </html>
                """.formatted(header(), name, otp, footer());
    }

    // ================= WELCOME EMAIL =================
    public static String welcomeEmail(String name, String url) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Welcome</title>
                </head>
                <body style="margin:0; padding:0; font-family:Arial; background:#f4f4f7;">
                
                    <div style="max-width:600px; margin:40px auto; background:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1);">
                
                        %s
                
                        <div style="padding:30px 20px; text-align:center; color:#333;">
                            <h1 style="color:#007BFF;">Hello, %s!</h1>
                
                            <p>Welcome to <b>Clinic Management System</b> — your trusted platform.</p>
                
                            <p>Please verify your email to continue.</p>
                
                            <a href="%s"
                               style="display:inline-block; margin-top:20px; padding:12px 25px;
                               background:#28a745; color:#fff; text-decoration:none;
                               border-radius:50px; font-weight:bold;">
                               Verify Email
                            </a>
                
                            <p style="margin-top:25px;">
                                If you need help, we are always here for you.
                            </p>
                        </div>
                
                        %s
                
                    </div>
                
                </body>
                </html>
                """.formatted(header(), name, url, footer());
    }

    public static String appointmentConfirmationEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String departmentName,
            String date,
            String time,
            String status,
            Double fee
    ) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0; padding:0; font-family:Arial; background:#f4f4f7;">
                    <div style="max-width:600px; margin:40px auto; background:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1);">
                        %s
                        <div style="padding:30px 20px; color:#333;">
                            <h1 style="color:#0d6efd; text-align:center;">Appointment Confirmed</h1>
                            <p>Hello <b>%s</b>,</p>
                            <p>Your doctor channeling appointment has been booked successfully.</p>
                            <table style="width:100%%; margin:20px 0; border-collapse:collapse;">
                                <tr><td style="padding:8px; border-bottom:1px solid #eee;"><b>Reference</b></td><td style="padding:8px; border-bottom:1px solid #eee;">%s</td></tr>
                                <tr><td style="padding:8px; border-bottom:1px solid #eee;"><b>Doctor</b></td><td style="padding:8px; border-bottom:1px solid #eee;">%s</td></tr>
                                <tr><td style="padding:8px; border-bottom:1px solid #eee;"><b>Department</b></td><td style="padding:8px; border-bottom:1px solid #eee;">%s</td></tr>
                                <tr><td style="padding:8px; border-bottom:1px solid #eee;"><b>Date</b></td><td style="padding:8px; border-bottom:1px solid #eee;">%s</td></tr>
                                <tr><td style="padding:8px; border-bottom:1px solid #eee;"><b>Time</b></td><td style="padding:8px; border-bottom:1px solid #eee;">%s</td></tr>
                                <tr><td style="padding:8px; border-bottom:1px solid #eee;"><b>Status</b></td><td style="padding:8px; border-bottom:1px solid #eee;">%s</td></tr>
                                <tr><td style="padding:8px;"><b>Fee (LKR)</b></td><td style="padding:8px;">%s</td></tr>
                            </table>
                            <p style="color:#666;">Please arrive 15 minutes early. You will receive a reminder 1 hour before your appointment.</p>
                        </div>
                        %s
                    </div>
                </body>
                </html>
                """.formatted(
                header(),
                patientName,
                appointmentNumber,
                doctorName,
                departmentName != null ? departmentName : "General",
                date,
                time,
                status,
                fee != null ? String.format("%.2f", fee) : "N/A",
                footer()
        );
    }

    public static String appointmentReminderEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String date,
            String time
    ) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0; padding:0; font-family:Arial; background:#f4f4f7;">
                    <div style="max-width:600px; margin:40px auto; background:#fff; border-radius:10px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.1);">
                        %s
                        <div style="padding:30px 20px; color:#333; text-align:center;">
                            <h1 style="color:#dc3545;">⏰ Appointment Reminder</h1>
                            <p>Hello <b>%s</b>,</p>
                            <p>Your appointment <b>%s</b> with <b>Dr. %s</b> is in <b>1 hour</b>.</p>
                            <p style="font-size:18px; color:#0d6efd;"><b>%s at %s</b></p>
                            <p>Please be at the clinic on time. Bring your reference number.</p>
                        </div>
                        %s
                    </div>
                </body>
                </html>
                """.formatted(header(), patientName, appointmentNumber, doctorName, date, time, footer());
    }

    public static String appointmentCancellationEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String date,
            String time
    ) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0; padding:0; font-family:Arial; background:#f4f4f7;">
                    <div style="max-width:600px; margin:40px auto; background:#fff; border-radius:10px; overflow:hidden;">
                        %s
                        <div style="padding:30px 20px; color:#333;">
                            <h1 style="color:#dc3545;">Appointment Cancelled</h1>
                            <p>Hello <b>%s</b>,</p>
                            <p>Your appointment <b>%s</b> with Dr. <b>%s</b> on <b>%s</b> at <b>%s</b> has been cancelled.</p>
                            <p>You can book a new slot on our channeling website.</p>
                        </div>
                        %s
                    </div>
                </body>
                </html>
                """.formatted(header(), patientName, appointmentNumber, doctorName, date, time, footer());
    }

    public static String appointmentRescheduleEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String date,
            String time
    ) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="margin:0; padding:0; font-family:Arial; background:#f4f4f7;">
                    <div style="max-width:600px; margin:40px auto; background:#fff; border-radius:10px; overflow:hidden;">
                        %s
                        <div style="padding:30px 20px; color:#333;">
                            <h1 style="color:#0d6efd;">Appointment Rescheduled</h1>
                            <p>Hello <b>%s</b>,</p>
                            <p>Your appointment <b>%s</b> with Dr. <b>%s</b> has been rescheduled to:</p>
                            <p style="font-size:18px;"><b>%s at %s</b></p>
                        </div>
                        %s
                    </div>
                </body>
                </html>
                """.formatted(header(), patientName, appointmentNumber, doctorName, date, time, footer());
    }

    public static String staffAccountCreatedEmail(
            String name,
            String email,
            String temporaryPassword,
            String verificationUrl,
            String resetPasswordUrl
    ) {

        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Staff Account Created</title>
                </head>
                <body style="margin:0;padding:0;font-family:Arial;background:#f4f4f7;">
                
                    <div style="
                        max-width:650px;
                        margin:40px auto;
                        background:#fff;
                        border-radius:10px;
                        overflow:hidden;
                        box-shadow:0 4px 15px rgba(0,0,0,.1);
                    ">
                
                        %s
                
                        <div style="padding:30px; color:#333;">
                
                            <h2 style="color:#0d6efd;">
                                Welcome %s
                            </h2>
                
                            <p>
                                A staff account has been created for you in the
                                <strong>Clinic Management System</strong>.
                            </p>
                
                            <div style="
                                background:#f8f9fa;
                                padding:20px;
                                border-radius:8px;
                                margin:20px 0;
                            ">
                                <p><strong>Login Email:</strong> %s</p>
                                <p><strong>Temporary Password:</strong> %s</p>
                            </div>
                
                            <p>
                                Please verify your email before accessing the system.
                            </p>
                
                            <div style="text-align:center;margin:25px 0;">
                                <a href="%s"
                                   style="
                                   background:#28a745;
                                   color:white;
                                   text-decoration:none;
                                   padding:12px 25px;
                                   border-radius:30px;
                                   font-weight:bold;">
                                   Verify Email
                                </a>
                            </div>
                
                            <div style="text-align:center;margin:25px 0;">
                                <a href="%s"
                                   style="
                                   background:#dc3545;
                                   color:white;
                                   text-decoration:none;
                                   padding:12px 25px;
                                   border-radius:30px;
                                   font-weight:bold;">
                                   Reset Password
                                </a>
                            </div>
                
                            <p>
                                For security reasons, please change your password immediately after your first login.
                            </p>
                
                            <p>
                                If you did not expect this account, contact your administrator.
                            </p>
                
                        </div>
                
                        %s
                
                    </div>
                
                </body>
                </html>
                """.formatted(
                header(),
                name,
                email,
                temporaryPassword,
                verificationUrl,
                resetPasswordUrl,
                footer()
        );
    }

    public static String walkInWelcomeEmail(String name, String email) {

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Welcome</title>
        </head>

        <body style="margin:0;padding:0;font-family:Arial;background:#f4f4f7;">

            <div style="max-width:600px;margin:40px auto;background:#fff;
                        border-radius:10px;overflow:hidden;
                        box-shadow:0 4px 15px rgba(0,0,0,0.1);">

                %s

                <div style="padding:30px 20px;text-align:center;color:#333;">

                    <h1 style="color:#28a745;">Welcome, %s 👋</h1>

                    <p>
                        Your patient account has been successfully created in
                        <b>Clinic Management System</b>.
                    </p>

                    <div style="background:#f8f9fa;padding:15px;margin:20px 0;
                                border-radius:8px;text-align:left;">

                        <p><b>Account Details:</b></p>
                        <p>Email: %s</p>

                        <p style="color:#28a745;font-weight:bold;">
                            Status: Active (No verification required)
                        </p>
                    </div>

                    <p>
                        You can now log in and book appointments anytime.
                    </p>

                    <a href="http://localhost:5173/login"
                       style="display:inline-block;margin-top:20px;
                       padding:12px 25px;background:#007bff;color:#fff;
                       text-decoration:none;border-radius:50px;font-weight:bold;">
                       Login Now
                    </a>

                    <p style="margin-top:25px;font-size:12px;color:#888;">
                        If you did not expect this email, please contact support.
                    </p>

                </div>

                %s

            </div>

        </body>
        </html>
    """.formatted(header(), name, email, footer());
    }

}
