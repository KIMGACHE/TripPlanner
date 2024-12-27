package com.tripPlanner.project.domain.signin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("cksals8731@naver.com");
        message.setSubject("Test Email");
        message.setText("This is a test email from Spring Boot.");
        message.setFrom("wlscksals@gmail.com");

        mailSender.send(message);
        System.out.println("Email sent successfully!");
    }
}
