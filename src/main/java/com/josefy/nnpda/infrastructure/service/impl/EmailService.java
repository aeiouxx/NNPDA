package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.service.IEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    @Value("${spring.mail.username}")
    private String from;
    private final JavaMailSender javaMailSender;
    @Override
    public void sendPasswordReset(String email, String token) {
        String subject = "Reset hesla";
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String resetUrl = "https://moje-domena.cz/reset-password?token=" + encodedToken;
        String body = "Dobrý den,\n\n" +
                "Pro reset hesla klikněte na následující odkaz:\n" + resetUrl + "\n\n" +
                "Pokud jste o reset hesla nežádali, prosím ignorujte tento email.\n\n" +
                "S pozdravem,\n" +
                "Já";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
}
