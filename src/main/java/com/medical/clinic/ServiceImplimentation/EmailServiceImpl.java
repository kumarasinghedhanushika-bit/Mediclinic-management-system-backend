package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.service.EmailServise;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailServise {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String htmlBody) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            // MUST MATCH SMTP USERNAME
            helper.setFrom("chavidairy@gmail.com");

            mailSender.send(message);

            System.out.println("Email sent successfully");

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Email sending failed: " + e.getMessage()
            );
        }
    }


}