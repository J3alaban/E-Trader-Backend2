package com.ecommerce.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordMail(String to, String token) {

        String link = "https://frontend-domain.com/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset");
        message.setText("Reset password link:\n" + link);

        mailSender.send(message);


    }


    public void sendVerificationMail(String to, String token) {

        String link = "https://frontend-domain.com/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email");
        message.setText("Click to verify:\n" + link);

        mailSender.send(message);
    }

}