package com.medical.clinic.ServiceImplimentation;

import com.medical.clinic.service.EmailServise;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements EmailServise {

    private final Resend resend;

    public EmailServiceImpl(@Value("${resend.api.key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    @Override
    public void sendEmail(String to, String subject, String htmlBody) {
        try {
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Medical Center <noreply@krishanidhanushika.com>")
                    .to(List.of(to))
                    .subject(subject)
                    .html(htmlBody)
                    .build();

            resend.emails().send(params);

        } catch (ResendException e) {
            System.out.println("email faild"+e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage());
        }
    }


}