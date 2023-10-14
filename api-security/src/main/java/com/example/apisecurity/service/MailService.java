package com.example.apisecurity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final String baseUrl;

    public MailService(JavaMailSender javaMailSender,@Value("http://localhost:8080") String baseUrl) {
        this.javaMailSender = javaMailSender;
        this.baseUrl = baseUrl;
    }

    public void sendMail(String email,String originalUrl,String token){
        String url = originalUrl != null? originalUrl : baseUrl;
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("jdc@javalab.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Reset Password");
        mailMessage.setText(String.format(
                "Click <a href=\"%s/reset/%s\">here</a> to reset your" +
                        "password.",
                url,
                token
        ));

        javaMailSender.send(mailMessage);
    }
}
