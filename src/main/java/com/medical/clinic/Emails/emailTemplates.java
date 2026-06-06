package com.medical.clinic.Emails;

import java.time.Year;

public class emailTemplates {

    // ================= SHARED STYLES =================
    private static final String BASE_FONT = "Georgia, 'Times New Roman', serif";
    private static final String SANS_FONT = "'Trebuchet MS', Arial, sans-serif";
    private static final String GREEN_DARK  = "#1e2d20";
    private static final String GOLD        = "#c8af78";
    private static final String GOLD_LIGHT  = "#e8dfc8";
    private static final String GOLD_FAINT  = "rgba(200,175,120,0.35)";
    private static final String CREAM       = "#fdfaf6";
    private static final String TEXT_DARK   = "#2c2318";
    private static final String TEXT_MID    = "#6b5e52";
    private static final String TEXT_MUTED  = "#a89e95";

    // ================= HEADER =================
    public static String header() {
        return """
                <div style="background:%s; padding:0; overflow:hidden;">
                    <div style="border-bottom:1px solid %s; padding:9px 40px;
                                display:flex; justify-content:space-between; align-items:center;">
                        <span style="font-family:%s; font-size:10px; letter-spacing:0.22em;
                                     color:rgba(200,175,120,0.55); font-weight:400;">EST. 2024</span>
                        <span style="font-family:%s; font-size:10px; letter-spacing:0.22em;
                                     color:rgba(200,175,120,0.55); font-weight:400;">REGISTERED &amp; CERTIFIED</span>
                    </div>
                    <div style="padding:26px 40px 30px; text-align:center;">
                        <div style="width:50px; height:1px; background:%s; margin:0 auto 14px;"></div>
                        <div style="font-family:%s; font-size:20px; font-weight:600;
                                    letter-spacing:0.18em; color:%s; text-transform:uppercase;">
                            Clinic Management</div>
                        <div style="font-family:%s; font-style:italic; font-size:13px;
                                    color:rgba(200,175,120,0.7); letter-spacing:0.12em; margin-top:5px;">
                            System</div>
                        <div style="display:flex; align-items:center; justify-content:center;
                                    gap:14px; margin-top:14px;">
                            <div style="flex:1; height:1px; background:rgba(200,175,120,0.22);"></div>
                            <span style="font-family:%s; font-size:10px; letter-spacing:0.28em;
                                         color:rgba(200,175,120,0.42);">FAST &middot; SECURE &middot; RELIABLE</span>
                            <div style="flex:1; height:1px; background:rgba(200,175,120,0.22);"></div>
                        </div>
                    </div>
                </div>
                """.formatted(
                GREEN_DARK, GOLD_FAINT,
                SANS_FONT, SANS_FONT,
                GOLD,
                BASE_FONT, GOLD_LIGHT,
                BASE_FONT,
                SANS_FONT
        );
    }

    // ================= FOOTER =================
    public static String footer() {
        return """
                <div style="background:%s; padding:0;">
                    <div style="border-top:1px solid %s; padding:22px 40px; text-align:center;">
                        <div style="font-family:%s; font-size:12px; letter-spacing:0.18em;
                                    color:%s; margin-bottom:10px; text-transform:uppercase;">
                            Clinic Management System</div>
                        <div style="width:36px; height:1px; background:%s;
                                    margin:0 auto 12px;"></div>
                        <div style="font-family:%s; font-size:12px; font-weight:400;
                                    color:rgba(200,175,120,0.55); letter-spacing:0.06em; line-height:1.8;">
                            Sri Lanka &nbsp;&bull;&nbsp; www.clinic.com</div>
                        <div style="font-family:%s; font-style:italic; font-size:12px;
                                    color:rgba(200,175,120,0.38); margin-top:10px; letter-spacing:0.04em;">
                            &copy; %s Clinic Management System. All rights reserved.</div>
                        <div style="font-family:%s; font-size:11px; font-weight:400;
                                    color:rgba(200,175,120,0.28); margin-top:7px; letter-spacing:0.04em;">
                            This is an automated email. Please do not reply.</div>
                    </div>
                </div>
                """.formatted(
                GREEN_DARK, GOLD_FAINT,
                BASE_FONT, GOLD,
                GOLD,
                SANS_FONT,
                BASE_FONT,
                Year.now().getValue(),
                SANS_FONT
        );
    }

    // ================= DIVIDER MOTIF =================
    private static String divider() {
        return """
                <div style="display:flex; align-items:center; gap:10px;
                            justify-content:center; margin:20px 0;">
                    <div style="width:28px; height:1px; background:rgba(200,175,120,0.4);"></div>
                    <div style="width:5px; height:5px; border:1px solid rgba(200,175,120,0.5);
                                transform:rotate(45deg);"></div>
                    <div style="width:28px; height:1px; background:rgba(200,175,120,0.4);"></div>
                </div>
                """;
    }

    // ================= SEAL ICON (checkmark) =================
    private static String sealIcon(String svgPath, String pathColor) {
        return """
                <div style="width:62px; height:62px; border-radius:50%; background:%s;
                            margin:0 auto 20px; display:flex; align-items:center;
                            justify-content:center; line-height:62px; text-align:center;">
                    <svg width="28" height="28" viewBox="0 0 30 30"
                         xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
                        <circle cx="15" cy="15" r="13.5"
                                stroke="%s" stroke-width="1.2" fill="none"/>
                        %s
                    </svg>
                </div>
                """.formatted(GREEN_DARK, GOLD, svgPath);
    }

    // ================= LABEL BADGE =================
    private static String badge(String text) {
        return """
                <div style="font-family:%s; font-size:10px; letter-spacing:0.3em;
                            color:%s; text-transform:uppercase; margin-bottom:10px;">%s</div>
                """.formatted(SANS_FONT, GOLD, text);
    }

    // ================= TABLE ROW =================
    private static String tableRow(String label, String value, boolean last) {
        String border = last ? "" : "border-bottom:1px solid rgba(200,175,120,0.2);";
        return """
                <tr>
                    <td style="padding:10px 14px; font-family:%s; font-size:13px;
                               color:%s; font-weight:400; %s width:38%%;">%s</td>
                    <td style="padding:10px 14px; font-family:%s; font-size:13px;
                               color:%s; font-weight:600; %s">%s</td>
                </tr>
                """.formatted(
                SANS_FONT, TEXT_MID, border, label,
                SANS_FONT, TEXT_DARK, border, value
        );
    }

    // ================= BUTTON =================
    private static String ctaButton(String href, String label, String bg, String textColor) {
        return """
                <a href="%s" style="display:inline-block; padding:13px 38px;
                    background:%s; color:%s; text-decoration:none;
                    font-family:%s; font-size:11px; letter-spacing:0.2em;
                    text-transform:uppercase; border:1px solid %s;">%s</a>
                """.formatted(href, bg, textColor, SANS_FONT, GOLD, label);
    }

    // ================= GOLD ACCENT LINE =================
    private static String accentLine() {
        return "<div style=\"height:3px; background:%s;\"></div>".formatted(GOLD);
    }

    // ================= WRAPPER OPEN =================
    private static String wrapperOpen() {
        return """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="margin:0; padding:0; font-family:%s; background:#ede8e0;">
                <div style="max-width:600px; margin:40px auto; background:%s;
                            border-radius:4px; overflow:hidden;
                            box-shadow:0 6px 32px rgba(0,0,0,0.18);">
                """.formatted(SANS_FONT, CREAM);
    }

    private static final String WRAPPER_CLOSE = "</div></body></html>";

    // ====================================================
    // ================= OTP EMAIL ========================
    // ====================================================
    public static String otpEmail(String name, String otp) {
        String svgPath = "<path d=\"M10 15.5l3.5 3.5 7-7\" stroke=\"%s\" stroke-width=\"1.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>".formatted(GOLD);
        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:28px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        OTP Verification</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:16px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s;
                               line-height:1.75; margin:0 0 6px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:13px; color:%s;
                               line-height:1.7; margin:0 0 28px;">
                        Use the one-time code below to verify your account.
                        This code is valid for a short time only.</p>

                    <div style="display:inline-block; background:%s;
                                border:1px solid %s; padding:18px 40px; margin:0 0 24px;">
                        <div style="font-family:%s; font-size:10px; letter-spacing:0.25em;
                                    color:rgba(200,175,120,0.7); margin-bottom:8px;
                                    text-transform:uppercase;">Your Code</div>
                        <div style="font-family:%s; font-size:34px; font-weight:600;
                                    letter-spacing:0.35em; color:%s;">%s</div>
                    </div>

                    <p style="font-family:%s; font-size:12px; color:%s;
                               margin-top:20px; letter-spacing:0.04em;">
                        If you did not request this, please ignore this email.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Secure Verification"),
                BASE_FONT, GREEN_DARK,
                BASE_FONT, TEXT_MID, GREEN_DARK, name,
                SANS_FONT, TEXT_MUTED,
                GREEN_DARK, GOLD_FAINT,
                SANS_FONT,
                BASE_FONT, GOLD_LIGHT, otp,
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ================= WELCOME EMAIL ====================
    // ====================================================
    public static String welcomeEmail(String name, String url) {
        String svgPath = "<path d=\"M10 15.5l3.5 3.5 7-7\" stroke=\"%s\" stroke-width=\"1.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>".formatted(GOLD);
        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:29px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Hello, %s!</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:16px auto;"></div>
                    <p style="font-family:%s; font-size:16px; color:%s; line-height:1.8; margin:0 0 8px;">
                        Welcome to <strong style="color:%s;">Clinic Management System</strong>
                        &mdash; your trusted platform for seamless healthcare booking.</p>
                    <p style="font-family:%s; font-size:13px; color:%s;
                               letter-spacing:0.03em; line-height:1.7; margin:0 0 28px;">
                        To complete your registration, please verify your email address
                        by clicking the button below.</p>
                    %s
                    <p style="font-family:%s; font-size:12px; color:%s; margin-top:24px;
                               letter-spacing:0.03em;">
                        If you need assistance, we are always here for you.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Welcome"),
                BASE_FONT, GREEN_DARK, name,
                BASE_FONT, TEXT_MID, GREEN_DARK,
                SANS_FONT, TEXT_MUTED,
                ctaButton(url, "Verify Email Address", GREEN_DARK, GOLD_LIGHT),
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ========= APPOINTMENT CONFIRMATION EMAIL ===========
    // ====================================================
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
        String svgPath = "<path d=\"M9 15l4 4 8-8\" stroke=\"%s\" stroke-width=\"1.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>".formatted(GOLD);
        String feeStr = (fee != null) ? String.format("LKR %.2f", fee) : "N/A";
        String dept   = (departmentName != null) ? departmentName : "General";

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 48px 28px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Appointment Confirmed</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:14px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 24px;">
                        Hello, <strong style="color:%s;">%s</strong> &mdash;
                        your appointment has been booked successfully.</p>
                </div>
                <div style="padding:0 48px 32px;">
                    <table style="width:100%%; border-collapse:collapse;
                                  border:1px solid rgba(200,175,120,0.3);">
                        <thead>
                            <tr style="background:%s;">
                                <td colspan="2" style="padding:10px 14px; font-family:%s;
                                    font-size:10px; letter-spacing:0.2em; color:%s;
                                    text-transform:uppercase;">Appointment Details</td>
                            </tr>
                        </thead>
                        <tbody>
                            %s %s %s %s %s %s %s
                        </tbody>
                    </table>
                    <p style="font-family:%s; font-size:12px; color:%s; margin-top:20px;
                               line-height:1.7; text-align:center; letter-spacing:0.03em;">
                        Please arrive 15 minutes early &middot;
                        Bring this reference number &middot;
                        A reminder will be sent 1 hour before your appointment.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Booking Confirmation"),
                BASE_FONT, GREEN_DARK,
                BASE_FONT, TEXT_MID, GREEN_DARK, patientName,
                GREEN_DARK, SANS_FONT, GOLD,
                tableRow("Reference No.", appointmentNumber, false),
                tableRow("Doctor", "Dr. " + doctorName, false),
                tableRow("Department", dept, false),
                tableRow("Date", date, false),
                tableRow("Time", time, false),
                tableRow("Status", status, false),
                tableRow("Consultation Fee", feeStr, true),
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ========= APPOINTMENT REMINDER EMAIL ===============
    // ====================================================
    public static String appointmentReminderEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String date,
            String time
    ) {
        String svgPath = "<path d=\"M15 9v6m0 3h.01\" stroke=\"%s\" stroke-width=\"1.5\" stroke-linecap=\"round\" fill=\"none\"/>".formatted(GOLD);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Appointment Reminder</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:14px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 20px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s; line-height:1.7; margin:0 0 20px;">
                        Your appointment <strong style="color:%s;">%s</strong>
                        with <strong style="color:%s;">Dr. %s</strong>
                        is scheduled in <strong style="color:%s;">1 hour</strong>.</p>

                    <div style="display:inline-block; background:%s;
                                border:1px solid %s; padding:16px 36px; margin:0 0 20px;">
                        <div style="font-family:%s; font-size:10px; letter-spacing:0.25em;
                                    color:rgba(200,175,120,0.7); margin-bottom:6px;
                                    text-transform:uppercase;">Date &amp; Time</div>
                        <div style="font-family:%s; font-size:20px; font-weight:600;
                                    color:%s; letter-spacing:0.06em;">%s &nbsp;&bull;&nbsp; %s</div>
                    </div>

                    <p style="font-family:%s; font-size:13px; color:%s;
                               margin-top:16px; letter-spacing:0.03em; line-height:1.7;">
                        Please be at the clinic on time and bring your reference number.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Reminder &mdash; 1 Hour"),
                BASE_FONT, GREEN_DARK,
                BASE_FONT, TEXT_MID, GREEN_DARK, patientName,
                BASE_FONT, TEXT_MID, GREEN_DARK, appointmentNumber, GREEN_DARK, doctorName, GREEN_DARK,
                GREEN_DARK, GOLD_FAINT,
                SANS_FONT,
                BASE_FONT, GOLD_LIGHT, date, time,
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ======= APPOINTMENT CANCELLATION EMAIL =============
    // ====================================================
    public static String appointmentCancellationEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String date,
            String time
    ) {
        String svgPath = "<path d=\"M11 11l8 8M19 11l-8 8\" stroke=\"%s\" stroke-width=\"1.5\" stroke-linecap=\"round\" fill=\"none\"/>".formatted(GOLD);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Appointment Cancelled</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:14px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 14px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s; line-height:1.8; margin:0 0 20px;">
                        Your appointment <strong style="color:%s;">%s</strong>
                        with <strong style="color:%s;">Dr. %s</strong>
                        on <strong style="color:%s;">%s at %s</strong> has been cancelled.</p>
                    <p style="font-family:%s; font-size:13px; color:%s;
                               line-height:1.7; margin:0 0 24px;">
                        You may rebook a new slot at any time through our website.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Cancellation Notice"),
                BASE_FONT, GREEN_DARK,
                BASE_FONT, TEXT_MID, GREEN_DARK, patientName,
                BASE_FONT, TEXT_MID,
                GREEN_DARK, appointmentNumber, GREEN_DARK, doctorName, GREEN_DARK, date, time,
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ======= APPOINTMENT RESCHEDULE EMAIL ===============
    // ====================================================
    public static String appointmentRescheduleEmail(
            String patientName,
            String appointmentNumber,
            String doctorName,
            String date,
            String time
    ) {
        String svgPath = "<path d=\"M7.5 21L3 16.5m0 0L7.5 12M3 16.5h13.5M16.5 3L21 7.5m0 0L16.5 12M21 7.5H7.5\" stroke=\"%s\" stroke-width=\"1.3\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>".formatted(GOLD);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Appointment Rescheduled</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:14px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 14px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s; line-height:1.8; margin:0 0 20px;">
                        Your appointment <strong style="color:%s;">%s</strong>
                        with <strong style="color:%s;">Dr. %s</strong>
                        has been rescheduled to a new date and time.</p>

                    <div style="display:inline-block; background:%s;
                                border:1px solid %s; padding:16px 36px; margin:0 0 20px;">
                        <div style="font-family:%s; font-size:10px; letter-spacing:0.25em;
                                    color:rgba(200,175,120,0.7); margin-bottom:6px;
                                    text-transform:uppercase;">New Schedule</div>
                        <div style="font-family:%s; font-size:20px; font-weight:600;
                                    color:%s; letter-spacing:0.06em;">%s &nbsp;&bull;&nbsp; %s</div>
                    </div>

                    <p style="font-family:%s; font-size:13px; color:%s;
                               margin-top:8px; letter-spacing:0.03em; line-height:1.7;">
                        Please update your schedule accordingly. See you soon.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Schedule Updated"),
                BASE_FONT, GREEN_DARK,
                BASE_FONT, TEXT_MID, GREEN_DARK, patientName,
                BASE_FONT, TEXT_MID,
                GREEN_DARK, appointmentNumber, GREEN_DARK, doctorName,
                GREEN_DARK, GOLD_FAINT,
                SANS_FONT,
                BASE_FONT, GOLD_LIGHT, date, time,
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ========= STAFF ACCOUNT CREATED EMAIL ==============
    // ====================================================
    public static String staffAccountCreatedEmail(
            String name,
            String email,
            String temporaryPassword,
            String verificationUrl,
            String resetPasswordUrl
    ) {
        String svgPath = "<path d=\"M12 12c2.7 0 4.8-2.1 4.8-4.8S14.7 2.4 12 2.4 7.2 4.5 7.2 7.2 9.3 12 12 12zm0 2.4c-3.2 0-9.6 1.6-9.6 4.8v2.4h19.2v-2.4c0-3.2-6.4-4.8-9.6-4.8z\" fill=\"%s\" opacity=\"0.85\"/>".formatted(GOLD);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 28px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Staff Account Created</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:14px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 6px;">
                        Welcome, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:13px; color:%s;
                               line-height:1.7; margin:0 0 24px;">
                        A staff account has been created for you in the
                        <strong style="color:%s;">Clinic Management System</strong>.</p>
                </div>
                <div style="padding:0 48px 32px;">
                    <table style="width:100%%; border-collapse:collapse;
                                  border:1px solid rgba(200,175,120,0.3); margin-bottom:28px;">
                        <thead>
                            <tr style="background:%s;">
                                <td colspan="2" style="padding:10px 14px; font-family:%s;
                                    font-size:10px; letter-spacing:0.2em; color:%s;
                                    text-transform:uppercase;">Login Credentials</td>
                            </tr>
                        </thead>
                        <tbody>
                            %s %s
                        </tbody>
                    </table>
                    <p style="font-family:%s; font-size:13px; color:%s; text-align:center;
                               margin:0 0 20px; line-height:1.7;">
                        Please verify your email before accessing the system.</p>
                    <div style="text-align:center; margin-bottom:14px;">
                        %s
                    </div>
                    <div style="text-align:center; margin-bottom:20px;">
                        %s
                    </div>
                    <p style="font-family:%s; font-size:12px; color:%s; text-align:center;
                               line-height:1.7; letter-spacing:0.03em;">
                        For security, please change your password immediately after first login.<br>
                        If you did not expect this account, contact your administrator.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Staff Onboarding"),
                BASE_FONT, GREEN_DARK,
                BASE_FONT, TEXT_MID, GREEN_DARK, name,
                SANS_FONT, TEXT_MUTED, GREEN_DARK,
                GREEN_DARK, SANS_FONT, GOLD,
                tableRow("Login Email", email, false),
                tableRow("Temporary Password", temporaryPassword, true),
                SANS_FONT, TEXT_MID,
                ctaButton(verificationUrl, "Verify Email", GREEN_DARK, GOLD_LIGHT),
                ctaButton(resetPasswordUrl, "Reset Password", CREAM, GREEN_DARK),
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ========= WALK-IN WELCOME EMAIL ====================
    // ====================================================
    public static String walkInWelcomeEmail(String name, String email) {
        String svgPath = "<path d=\"M10 15.5l3.5 3.5 7-7\" stroke=\"%s\" stroke-width=\"1.5\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>".formatted(GOLD);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:36px 52px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:28px; font-weight:600;
                                color:%s; letter-spacing:0.04em; line-height:1.2;">
                        Welcome, %s!</div>
                    <div style="width:40px; height:1px; background:rgba(200,175,120,0.6);
                                margin:16px auto;"></div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.8; margin:0 0 22px;">
                        Your patient account has been successfully created in
                        <strong style="color:%s;">Clinic Management System</strong>.</p>
                </div>
                <div style="padding:0 48px 32px;">
                    <table style="width:100%%; border-collapse:collapse;
                                  border:1px solid rgba(200,175,120,0.3); margin-bottom:24px;">
                        <thead>
                            <tr style="background:%s;">
                                <td colspan="2" style="padding:10px 14px; font-family:%s;
                                    font-size:10px; letter-spacing:0.2em; color:%s;
                                    text-transform:uppercase;">Account Details</td>
                            </tr>
                        </thead>
                        <tbody>
                            %s %s
                        </tbody>
                    </table>
                    <div style="text-align:center; margin-bottom:20px;">
                        %s
                    </div>
                    <p style="font-family:%s; font-size:12px; color:%s; text-align:center;
                               letter-spacing:0.03em; line-height:1.7;">
                        If you did not expect this email, please contact support.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, GOLD),
                badge("Patient Account"),
                BASE_FONT, GREEN_DARK, name,
                BASE_FONT, TEXT_MID, GREEN_DARK,
                GREEN_DARK, SANS_FONT, GOLD,
                tableRow("Email", email, false),
                tableRow("Status", "Active &mdash; No verification required", true),
                ctaButton("http://localhost:5173/login", "Login to Your Account", GREEN_DARK, GOLD_LIGHT),
                SANS_FONT, TEXT_MUTED,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }
}