package com.compute.rental.modules.auth.service;

import com.compute.rental.modules.auth.support.AuthProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoggingEmailService implements EmailService {

    private final JavaMailSender mailSender;
    private final AuthProperties authProperties;

    public LoggingEmailService(JavaMailSender mailSender, AuthProperties authProperties) {
        this.mailSender = mailSender;
        this.authProperties = authProperties;
    }

    @Override
    public void sendLoginCode(String email, String code) {
        var message = new SimpleMailMessage();
        if (StringUtils.hasText(authProperties.from())) {
            message.setFrom(authProperties.from());
        }
        message.setTo(email);
        message.setSubject(authProperties.subject());
        message.setText("""
                Your login verification code is: %s

                This code expires in %d minutes. If you did not request this code, please ignore this email.
                """.formatted(code, authProperties.codeTtl().toMinutes()));
        mailSender.send(message);
    }
}
