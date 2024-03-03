package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.model.OtpEntity;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.repo.OtpRepository;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import com.NaukriChowk.Job_Wala.utils.EmailUtil;
import com.NaukriChowk.Job_Wala.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    public void saveOtp(String email, String otp) {

        try{
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otp);
            otpEntity.setOtpGeneratedTime(LocalDateTime.now());
            otpRepository.save(otpEntity);
        }
        catch (DataIntegrityViolationException e) {
            handleDuplicateEmail(email, otp);
        }
    }

    private void handleDuplicateEmail(String email, String otp) {
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);
        optionalOtpEntity.ifPresentOrElse(
                entity -> {
                    // Update the existing entity with the new OTP value or handle the scenario as needed
                    entity.setOtp(otp);
                    otpRepository.save(entity);
                },
                () -> {
                    // Log an error or throw a custom exception if needed
                }
        );

    }


    public boolean verifyOtp(String email, String enteredOtp) {
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);
        return optionalOtpEntity.map(otpEntity -> otpEntity.getOtp().equals(enteredOtp)).orElse(false);
    }


    public String regenerateOtp(String email) {

        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);
        if (optionalOtpEntity.isPresent()) {

            OtpEntity otpEntity = optionalOtpEntity.get();
            LocalDateTime otpGeneratedTime = otpEntity.getOtpGeneratedTime();
            LocalDateTime currentTime = LocalDateTime.now();
            Duration duration = Duration.between(otpGeneratedTime, currentTime);
            long otpExpirationMinutes = 2; // Change this value according to your expiration time
            if (duration.toMinutes() >= otpExpirationMinutes) {
                String otp = otpUtil.generateOtp();
                otpEntity.setOtp(otp);
                otpEntity.setOtpGeneratedTime(LocalDateTime.now());
                otpRepository.save(otpEntity);
                emailUtil.sendOtpEmail(email, otp);
                return "Email sent... please verify account within 2 minutes";
            } else {
                return "OTP is still valid. No need to regenerate.";
            }
        } else {
            throw new RuntimeException("No OTP found for this email: " + email);
        }
    }
       /* String otp = otpUtil.generateOtp();
        emailUtil.sendOtpEmail(email, otp);

        try{
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otp);
            otpEntity.setOtpGeneratedTime(LocalDateTime.now());
            otpRepository.save(otpEntity);
        }
        catch (DataIntegrityViolationException e) {
            handleDuplicateEmail(email, otp);
        }
        return "Email sent... please verify account within 2 minute";

        */
    }


