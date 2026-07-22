package com.medical.clinic.Emails;

import java.time.Year;

public class emailTemplates {

    // ================= SHARED STYLES =================
    private static final String BASE_FONT   = "'Segoe UI', 'Helvetica Neue', Arial, sans-serif";
    private static final String MONO_FONT   = "'Courier New', Courier, monospace";

    // ---- Modern Sky-Blue Theme Palette ----
    private static final String TEAL        = "#0ea5e9"; // sky-500 (primary accent)
    private static final String TEAL_DARK   = "#0284c7"; // sky-600
    private static final String TEAL_LIGHT  = "#bae6fd"; // sky-200
    private static final String TEAL_FAINT  = "#f0f9ff"; // sky-50
    private static final String BLUE        = "#38bdf8"; // sky-400 (gradient partner)
    private static final String BLUE_LIGHT  = "#e0f2fe"; // sky-100
    private static final String WHITE       = "#ffffff";
    private static final String GRAY_50     = "#f8fafc";
    private static final String GRAY_100    = "#f1f5f9";
    private static final String GRAY_200    = "#e2e8f0";
    private static final String GRAY_400    = "#94a3b8";
    private static final String GRAY_500    = "#64748b";
    private static final String GRAY_700    = "#334155";
    private static final String GRAY_900    = "#0f172a";

    // ================= HEADER =================
    public static String header() {
        return """
                <div style="background:%s; border-bottom:3px solid %s; padding:0;">
                    <div style="padding:32px 48px 28px; text-align:center;">
                        <div style="display:inline-flex; align-items:center; gap:10px;
                                    justify-content:center; margin-bottom:6px;">
                            <div style="width:38px; height:38px; background:linear-gradient(135deg,%s,%s);
                                        border-radius:12px; display:inline-block;
                                        line-height:38px; text-align:center; box-shadow:0 4px 10px rgba(14,165,233,0.28);">
                                <svg width="20" height="20" viewBox="0 0 24 24"
                                     xmlns="http://www.w3.org/2000/svg"
                                     style="vertical-align:middle; margin-top:-1px;">
                                    <path d="M19 3H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2zm-7 3a1 1 0 0 1 1 1v3h3a1 1 0 0 1 0 2h-3v3a1 1 0 0 1-2 0v-3H8a1 1 0 0 1 0-2h3V7a1 1 0 0 1 1-1z"
                                          fill="%s"/>
                                </svg>
                            </div>
                            <div>
                                <div style="font-family:%s; font-size:18px; font-weight:700;
                                            color:%s; letter-spacing:-0.01em;">
                                    Clinic Management</div>
                                <div style="font-family:%s; font-size:11px; color:%s;
                                            letter-spacing:0.08em; text-transform:uppercase;
                                            margin-top:1px;">System</div>
                            </div>
                        </div>
                    </div>
                    <div style="background:%s; padding:7px 48px; text-align:center;">
                        <span style="font-family:%s; font-size:11px; color:%s;
                                     letter-spacing:0.05em;">
                            Fast &nbsp;&middot;&nbsp; Secure &nbsp;&middot;&nbsp; Reliable
                        </span>
                    </div>
                </div>
                """.formatted(
                WHITE, TEAL_LIGHT,
                TEAL, BLUE, WHITE,
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_500,
                TEAL_FAINT, BASE_FONT, TEAL_DARK
        );
    }

    // ================= FOOTER =================
    public static String footer() {
        return """
                <div style="background:%s; border-top:1px solid %s; padding:28px 48px; text-align:center;">
                    <div style="font-family:%s; font-size:13px; font-weight:600;
                                color:%s; margin-bottom:6px;">
                        Clinic Management System</div>
                    <div style="font-family:%s; font-size:12px; color:%s;
                                margin-bottom:10px;">
                        Sri Lanka &nbsp;&bull;&nbsp; http://www.mediclinic.krishanidhanushika.com</div>
                    <div style="font-family:%s; font-size:11px; color:%s; margin-bottom:4px;">
                        &copy; %s Clinic Management System. All rights reserved.</div>
                    <div style="font-family:%s; font-size:11px; color:%s;">
                        This is an automated email. Please do not reply.</div>
                </div>
                """.formatted(
                GRAY_50, GRAY_200,
                BASE_FONT, GRAY_700,
                BASE_FONT, GRAY_500,
                BASE_FONT, GRAY_400,
                Year.now().getValue(),
                BASE_FONT, GRAY_400
        );
    }

    // ================= DIVIDER =================
    private static String divider() {
        return """
                <div style="border-top:1px solid %s; margin:24px 0;"></div>
                """.formatted(GRAY_200);
    }

    // ====================================================
// ============ PAYHERE PAYMENT EMAIL ================
// ====================================================

    public static String payherePaymentEmail(
            String patientName,
            String orderId,
            String amount,
            String currency,
            String status,
            String message
    ) {

        String color;
        String title;
        String icon;

        switch (status) {
            case "2" -> { // SUCCESS
                color = "#16a34a";
                title = "Payment Successful";
                icon = "✔";
            }
            case "0" -> { // PENDING
                color = "#f59e0b";
                title = "Payment Pending";
                icon = "⏳";
            }
            case "-1" -> { // CANCELLED
                color = "#dc2626";
                title = "Payment Cancelled";
                icon = "✖";
            }
            case "-2" -> { // FAILED
                color = "#b91c1c";
                title = "Payment Failed";
                icon = "⚠";
            }
            default -> {
                color = "#64748b";
                title = "Payment Status Update";
                icon = "ℹ";
            }
        }

        return wrapperOpen() + header() + accentLine() + """
        <div style="padding:40px 48px; text-align:center;">

            <div style="font-size:50px; color:%s; margin-bottom:10px;">%s</div>

            <div style="font-family:%s; font-size:26px; font-weight:700; color:%s;">
                %s
            </div>

            <p style="font-family:%s; font-size:15px; color:%s; margin-top:10px;">
                Hello <strong>%s</strong>, your payment update is below.
            </p>
        </div>

        <div style="padding:0 48px 30px;">
<<<<<<< HEAD
            <table style="width:100%%; border-collapse:separate; border-spacing:0; border:1px solid %s; border-radius:10px; overflow:hidden;">
=======
            <table style="width:100%%; border-collapse:collapse; border:1px solid %s;">
>>>>>>> a789e81295aa029a099b3c63df8eec257aff8de7
                <tbody>
                    %s
                    %s
                    %s
                    %s
                    %s
                </tbody>
            </table>
        </div>
    """.formatted(
                color, icon,
                BASE_FONT, color, title,
                BASE_FONT, GRAY_700,
                patientName,
                GRAY_200,

                tableRow("Order ID", orderId, false),
                tableRow("Amount", amount + " " + currency, false),
                tableRow("Status Code", status, false),
                tableRow("Message", message != null ? message : "N/A", false),
                tableRow("Payment Status", title, true)
        ) + footer() + WRAPPER_CLOSE;
    }

    // ================= ICON CIRCLE =================
    private static String sealIcon(String svgPath, String pathColor) {
        return """
                <div style="width:68px; height:68px; border-radius:50%%; background:linear-gradient(135deg,%s,%s);
                            margin:0 auto 20px; display:flex; align-items:center;
                            justify-content:center; line-height:68px; text-align:center;
                            box-shadow:0 6px 16px rgba(14,165,233,0.18);">
                    <svg width="30" height="30" viewBox="0 0 30 30"
                         xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
                        %s
                    </svg>
                </div>
                """.formatted(TEAL_FAINT, TEAL_LIGHT, svgPath);
    }

    // ================= LABEL BADGE =================
    private static String badge(String text) {
        return """
                <div style="display:inline-block; background:%s; color:%s;
                            font-family:%s; font-size:11px; font-weight:600;
                            letter-spacing:0.06em; text-transform:uppercase;
                            padding:5px 14px; border-radius:999px; margin-bottom:14px;">%s</div>
                """.formatted(TEAL_FAINT, TEAL_DARK, BASE_FONT, text);
    }

    // ================= TABLE ROW =================
    private static String tableRow(String label, String value, boolean last) {
        String border = last ? "" : "border-bottom:1px solid " + GRAY_100 + ";";
        return "<tr>"
                + "<td style=\"padding:12px 16px; font-family:" + BASE_FONT + "; font-size:13px;"
                + " color:" + GRAY_500 + "; font-weight:400; background:" + GRAY_50 + "; " + border + " width:40%;\">" + label + "</td>"
                + "<td style=\"padding:12px 16px; font-family:" + BASE_FONT + "; font-size:13px;"
                + " color:" + GRAY_900 + "; font-weight:600; background:" + WHITE + "; " + border + "\">" + value + "</td>"
                + "</tr>";
    }

    // ================= BUTTON =================
    private static String ctaButton(String href, String label, String bg, String textColor) {
        return """
                <a href="%s" style="display:inline-block; padding:13px 34px;
                    background:%s; color:%s; text-decoration:none;
                    font-family:%s; font-size:13px; font-weight:600;
                    letter-spacing:0.03em; border-radius:10px; box-shadow:0 4px 12px rgba(14,165,233,0.22);">%s</a>
                """.formatted(href, bg, textColor, BASE_FONT, label);
    }

    // ================= ACCENT LINE =================
    private static String accentLine() {
        return "<div style=\"height:4px; background:linear-gradient(90deg,%s,%s);\"></div>".formatted(TEAL, BLUE);
    }

    // ================= WRAPPER OPEN =================
    private static String wrapperOpen() {
        return """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="margin:0; padding:0; font-family:%s; background:%s;">
                <div style="max-width:600px; margin:40px auto; background:%s;
                            border-radius:18px; overflow:hidden;
                            box-shadow:0 8px 30px rgba(14,165,233,0.12); border:1px solid %s;">
                """.formatted(BASE_FONT, TEAL_FAINT, WHITE, GRAY_200);
    }

    private static final String WRAPPER_CLOSE = "</div></body></html>";

    // ====================================================
    // ================= OTP EMAIL ========================
    // ====================================================
    public static String otpEmail(String name, String otp) {
        String svgPath = "<path d=\"M8 15l4.5 4.5L22 9\" stroke=\"" + TEAL + "\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>";
        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Verify Your Account</div>
                    <p style="font-family:%s; font-size:15px; color:%s;
                               line-height:1.7; margin:0 0 6px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s;
                               line-height:1.7; margin:0 0 28px;">
                        Use the one-time code below to verify your account.
                        This code is valid for a short time only.</p>

                    <div style="background:%s; border:2px solid %s;
                                border-radius:14px; padding:20px 40px; margin:0 0 24px;
                                display:inline-block;">
                        <div style="font-family:%s; font-size:11px; font-weight:600;
                                    letter-spacing:0.1em; color:%s; margin-bottom:8px;
                                    text-transform:uppercase;">Your Verification Code</div>
                        <div style="font-family:%s; font-size:36px; font-weight:700;
                                    letter-spacing:0.3em; color:%s;">%s</div>
                    </div>

                    <p style="font-family:%s; font-size:12px; color:%s;
                               margin-top:16px; line-height:1.6;">
                        If you did not request this, please ignore this email.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, TEAL),
                badge("Secure Verification"),
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_700, TEAL_DARK, name,
                BASE_FONT, GRAY_500,
                TEAL_FAINT, TEAL_LIGHT,
                BASE_FONT, TEAL,
                MONO_FONT, TEAL_DARK, otp,
                BASE_FONT, GRAY_400,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ================= WELCOME EMAIL ====================
    // ====================================================
    public static String welcomeEmail(String name, String url) {
        String svgPath = "<path d=\"M8 15l4.5 4.5L22 9\" stroke=\"" + TEAL + "\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>";
        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Welcome, %s!</div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.8; margin:0 0 8px;">
                        Welcome to <strong style="color:%s;">Clinic Management System</strong>
                        &mdash; your trusted platform for seamless healthcare booking.</p>
                    <p style="font-family:%s; font-size:14px; color:%s;
                               line-height:1.7; margin:0 0 28px;">
                        To complete your registration, please verify your email address
                        by clicking the button below.</p>
                    %s
                    <p style="font-family:%s; font-size:12px; color:%s; margin-top:20px;
                               line-height:1.6;">
                        If you need assistance, we are always here for you.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, TEAL),
                badge("Welcome"),
                BASE_FONT, GRAY_900, name,
                BASE_FONT, GRAY_700, TEAL_DARK,
                BASE_FONT, GRAY_500,
                ctaButton(url, "Verify Email Address", TEAL, WHITE),
                BASE_FONT, GRAY_400,
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
        String svgPath = "<path d=\"M8 15l4.5 4.5L22 9\" stroke=\"" + TEAL + "\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>";
        String feeStr = (fee != null) ? String.format("LKR %.2f", fee) : "N/A";
        String dept   = (departmentName != null) ? departmentName : "General";

        // Build table rows first (plain string concatenation — no % format tokens)
        String rows = tableRow("Reference No.", appointmentNumber, false)
                + tableRow("Doctor", "Dr. " + doctorName, false)
                + tableRow("Department", dept, false)
                + tableRow("Date", date, false)
                + tableRow("Time", time, false)
                + tableRow("Status", status, false)
                + tableRow("Consultation Fee", feeStr, true);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 28px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Appointment Confirmed</div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 24px;">
                        Hello, <strong style="color:%s;">%s</strong> &mdash;
                        your appointment has been booked successfully.</p>
                </div>
                <div style="padding:0 48px 32px;">
                    <table style="width:100%%; border-collapse:separate; border-spacing:0;
                                  border:1px solid %s; border-radius:12px; overflow:hidden;">
                        <thead>
                            <tr style="background:%s;">
                                <td colspan="2" style="padding:12px 16px; font-family:%s;
                                    font-size:11px; font-weight:700; letter-spacing:0.08em;
                                    color:%s; text-transform:uppercase;">Appointment Details</td>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                    <div style="background:%s; border-left:3px solid %s;
                                border-radius:8px; padding:12px 16px; margin-top:20px;">
                        <p style="font-family:%s; font-size:12px; color:%s; margin:0;
                                   line-height:1.7;">
                            Please arrive 15 minutes early &nbsp;&middot;&nbsp;
                            Bring this reference number &nbsp;&middot;&nbsp;
                            A reminder will be sent 1 hour before your appointment.</p>
                    </div>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, TEAL),
                badge("Booking Confirmation"),
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_700, TEAL_DARK, patientName,
                GRAY_200,
                TEAL_FAINT, BASE_FONT, TEAL_DARK,
                rows,
                BLUE_LIGHT, TEAL,
                BASE_FONT, GRAY_700,
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
        String svgPath = "<circle cx=\"15\" cy=\"15\" r=\"6\" stroke=\"" + TEAL + "\" stroke-width=\"2\" fill=\"none\"/><path d=\"M15 12v3.5l2 2\" stroke=\"" + TEAL + "\" stroke-width=\"1.8\" stroke-linecap=\"round\" fill=\"none\"/>";

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Appointment Reminder</div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 16px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s; line-height:1.7; margin:0 0 24px;">
                        Your appointment <strong style="color:%s;">%s</strong>
                        with <strong style="color:%s;">Dr. %s</strong>
                        is scheduled in <strong style="color:%s;">1 hour</strong>.</p>

                    <div style="background:%s; border:2px solid %s;
                                border-radius:14px; padding:18px 36px; margin:0 0 20px;
                                display:inline-block;">
                        <div style="font-family:%s; font-size:11px; font-weight:600;
                                    letter-spacing:0.08em; color:%s; margin-bottom:6px;
                                    text-transform:uppercase;">Date &amp; Time</div>
                        <div style="font-family:%s; font-size:20px; font-weight:700;
                                    color:%s;">%s &nbsp;&bull;&nbsp; %s</div>
                    </div>

                    <p style="font-family:%s; font-size:13px; color:%s;
                               margin-top:12px; line-height:1.7;">
                        Please be at the clinic on time and bring your reference number.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, TEAL),
                badge("Reminder \u2014 1 Hour"),
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_700, TEAL_DARK, patientName,
                BASE_FONT, GRAY_700, TEAL_DARK, appointmentNumber, TEAL_DARK, doctorName, TEAL_DARK,
                TEAL_FAINT, TEAL_LIGHT,
                BASE_FONT, TEAL,
                BASE_FONT, GRAY_900, date, time,
                BASE_FONT, GRAY_500,
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
        String RED_SOFT  = "#fef2f2";
        String RED_BORDER = "#fecaca";
        String RED       = "#dc2626";
        String svgPath = "<path d=\"M10 10l10 10M20 10l-10 10\" stroke=\"" + RED + "\" stroke-width=\"2\" stroke-linecap=\"round\" fill=\"none\"/>";

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 32px; text-align:center;">
                    <div style="width:68px; height:68px; border-radius:50%%; background:%s;
                                margin:0 auto 20px; display:flex; align-items:center;
                                justify-content:center; line-height:68px; text-align:center;">
                        <svg width="30" height="30" viewBox="0 0 30 30"
                             xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
                            %s
                        </svg>
                    </div>
                    <div style="display:inline-block; background:%s; color:%s;
                                font-family:%s; font-size:11px; font-weight:600;
                                letter-spacing:0.06em; text-transform:uppercase;
                                padding:5px 14px; border-radius:999px; margin-bottom:14px;">Cancellation Notice</div>
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Appointment Cancelled</div>
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
                RED_SOFT,
                svgPath,
                RED_SOFT, RED,
                BASE_FONT,
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_700, RED, patientName,
                BASE_FONT, GRAY_700,
                RED, appointmentNumber, RED, doctorName, RED, date, time,
                BASE_FONT, GRAY_500,
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
        String AMBER      = "#f59e0b";
        String AMBER_SOFT = "#fffbeb";
        String AMBER_BORDER = "#fde68a";
        String svgPath = "<path d=\"M7 21L3 17m0 0l4-4M3 17h13M17 3l4 4m0 0l-4 4M21 7H8\" stroke=\"" + AMBER + "\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>";

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 32px; text-align:center;">
                    <div style="width:68px; height:68px; border-radius:50%%; background:%s;
                                margin:0 auto 20px; display:flex; align-items:center;
                                justify-content:center; line-height:68px; text-align:center;">
                        <svg width="30" height="30" viewBox="0 0 30 30"
                             xmlns="http://www.w3.org/2000/svg" style="vertical-align:middle;">
                            %s
                        </svg>
                    </div>
                    <div style="display:inline-block; background:%s; color:%s;
                                font-family:%s; font-size:11px; font-weight:600;
                                letter-spacing:0.06em; text-transform:uppercase;
                                padding:5px 14px; border-radius:999px; margin-bottom:14px;">Schedule Updated</div>
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Appointment Rescheduled</div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 14px;">
                        Hello, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s; line-height:1.8; margin:0 0 20px;">
                        Your appointment <strong style="color:%s;">%s</strong>
                        with <strong style="color:%s;">Dr. %s</strong>
                        has been rescheduled to a new date and time.</p>

                    <div style="background:%s; border:2px solid %s;
                                border-radius:14px; padding:18px 36px; margin:0 0 20px;
                                display:inline-block;">
                        <div style="font-family:%s; font-size:11px; font-weight:600;
                                    letter-spacing:0.08em; color:%s; margin-bottom:6px;
                                    text-transform:uppercase;">New Schedule</div>
                        <div style="font-family:%s; font-size:20px; font-weight:700;
                                    color:%s;">%s &nbsp;&bull;&nbsp; %s</div>
                    </div>

                    <p style="font-family:%s; font-size:13px; color:%s;
                               margin-top:8px; line-height:1.7;">
                        Please update your schedule accordingly. See you soon.</p>
                    %s
                </div>
                """.formatted(
                AMBER_SOFT,
                svgPath,
                AMBER_SOFT, AMBER,
                BASE_FONT,
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_700, AMBER, patientName,
                BASE_FONT, GRAY_700,
                AMBER, appointmentNumber, AMBER, doctorName,
                AMBER_SOFT, AMBER_BORDER,
                BASE_FONT, AMBER,
                BASE_FONT, GRAY_900, date, time,
                BASE_FONT, GRAY_500,
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
        String svgPath = "<path d=\"M15 15c2.5 0 4.5-2 4.5-4.5S17.5 6 15 6s-4.5 2-4.5 4.5S12.5 15 15 15zm0 2.25c-3 0-9 1.5-9 4.5v2.25h18v-2.25c0-3-6-4.5-9-4.5z\" fill=\"" + TEAL + "\" opacity=\"0.9\"/>";

        // Build rows without format tokens to avoid stray-% issues
        String rows = tableRow("Login Email", email, false)
                + tableRow("Temporary Password", temporaryPassword, true);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 28px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Staff Account Created</div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.75; margin:0 0 6px;">
                        Welcome, <strong style="color:%s;">%s</strong></p>
                    <p style="font-family:%s; font-size:14px; color:%s;
                               line-height:1.7; margin:0 0 24px;">
                        A staff account has been created for you in the
                        <strong style="color:%s;">Clinic Management System</strong>.</p>
                </div>
                <div style="padding:0 48px 32px;">
                    <table style="width:100%%; border-collapse:separate; border-spacing:0;
                                  border:1px solid %s; border-radius:12px;
                                  overflow:hidden; margin-bottom:24px;">
                        <thead>
                            <tr style="background:%s;">
                                <td colspan="2" style="padding:12px 16px; font-family:%s;
                                    font-size:11px; font-weight:700; letter-spacing:0.08em;
                                    color:%s; text-transform:uppercase;">Login Credentials</td>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                    <p style="font-family:%s; font-size:13px; color:%s; text-align:center;
                               margin:0 0 16px; line-height:1.7;">
                        Please verify your email before accessing the system.</p>
                    <div style="text-align:center; margin-bottom:12px;">
                        %s
                    </div>
                    <div style="text-align:center; margin-bottom:20px;">
                        %s
                    </div>
                    <div style="background:%s; border-left:3px solid %s;
                                border-radius:8px; padding:12px 16px;">
                        <p style="font-family:%s; font-size:12px; color:%s; margin:0;
                                   line-height:1.7;">
                            For security, please change your password immediately after first login.<br>
                            If you did not expect this account, contact your administrator.</p>
                    </div>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, TEAL),
                badge("Staff Onboarding"),
                BASE_FONT, GRAY_900,
                BASE_FONT, GRAY_700, TEAL_DARK, name,
                BASE_FONT, GRAY_500, TEAL_DARK,
                GRAY_200,
                TEAL_FAINT, BASE_FONT, TEAL_DARK,
                rows,
                BASE_FONT, GRAY_700,
                ctaButton(verificationUrl, "Verify Email", TEAL, WHITE),
                ctaButton(resetPasswordUrl, "Reset Password", WHITE, TEAL_DARK),
                BLUE_LIGHT, TEAL,
                BASE_FONT, GRAY_700,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }

    // ====================================================
    // ========= WALK-IN WELCOME EMAIL ====================
    // ====================================================
    public static String walkInWelcomeEmail(String name, String email) {
        String svgPath = "<path d=\"M8 15l4.5 4.5L22 9\" stroke=\"" + TEAL + "\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\" fill=\"none\"/>";

        // Build rows without format tokens
        String rows = tableRow("Email", email, false)
                + tableRow("Status", "Active &mdash; No verification required", true);

        return wrapperOpen() + header() + accentLine() + """
                <div style="padding:40px 48px 32px; text-align:center;">
                    %s
                    %s
                    <div style="font-family:%s; font-size:26px; font-weight:700;
                                color:%s; letter-spacing:-0.02em; margin-bottom:10px;">
                        Welcome, %s!</div>
                    <p style="font-family:%s; font-size:15px; color:%s; line-height:1.8; margin:0 0 22px;">
                        Your patient account has been successfully created in
                        <strong style="color:%s;">Clinic Management System</strong>.</p>
                </div>
                <div style="padding:0 48px 32px;">
                    <table style="width:100%%; border-collapse:separate; border-spacing:0;
                                  border:1px solid %s; border-radius:12px;
                                  overflow:hidden; margin-bottom:24px;">
                        <thead>
                            <tr style="background:%s;">
                                <td colspan="2" style="padding:12px 16px; font-family:%s;
                                    font-size:11px; font-weight:700; letter-spacing:0.08em;
                                    color:%s; text-transform:uppercase;">Account Details</td>
                        </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                    <div style="text-align:center; margin-bottom:20px;">
                        %s
                    </div>
                    <p style="font-family:%s; font-size:12px; color:%s; text-align:center;
                               line-height:1.7;">
                        If you did not expect this email, please contact support.</p>
                    %s
                </div>
                """.formatted(
                sealIcon(svgPath, TEAL),
                badge("Patient Account"),
                BASE_FONT, GRAY_900, name,
                BASE_FONT, GRAY_700, TEAL_DARK,
                GRAY_200,
                TEAL_FAINT, BASE_FONT, TEAL_DARK,
                rows,
                ctaButton("http://localhost:5173/login", "Login to Your Account", TEAL, WHITE),
                BASE_FONT, GRAY_400,
                divider()
        ) + footer() + WRAPPER_CLOSE;
    }
}