package com.NaukriChowk.Job_Wala.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public boolean sendOtpEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification");
            message.setText("Your OTP for registration is: " + otp);
            javaMailSender.send(message);
            return true; // Email sending successful
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace(); // Consider using a logging framework like SLF4J or java.util.logging
            return false; // Email sending failed
        }
    }

}
