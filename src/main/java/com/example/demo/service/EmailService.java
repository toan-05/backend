package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendResetToken(String to, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Dat lai mat khau");
            message.setText("Ma dat lai mat khau: " + token);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Khong gui duoc email reset password to {}: {}", to, e.getMessage());
            throw new RuntimeException("Không gửi được email đặt lại mật khẩu, vui lòng thử lại sau");
        }
    }
}
