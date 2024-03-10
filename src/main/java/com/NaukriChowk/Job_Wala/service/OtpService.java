package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.authrequest.RegenerateOtp;
import com.NaukriChowk.Job_Wala.model.OtpEntity;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.repo.OtpRepository;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import com.NaukriChowk.Job_Wala.utils.EmailUtil;
import com.NaukriChowk.Job_Wala.utils.OtpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    public void saveOtp(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not exist with this email"));
        try{
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otp);
            otpEntity.setOtpGeneratedTime(LocalDateTime.now());
            otpEntity.setUser(user);
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
                    entity.setOtpGeneratedTime(LocalDateTime.now());
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


    public String regenerateOtp(RegenerateOtp regenerate) {

        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(regenerate.getEmail());
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
                emailUtil.sendOtpEmail(regenerate.getEmail(), otp);
                return "Email sent... please verify account within 2 minutes";
            } else {
                return "OTP is still valid. No need to regenerate.";
            }
        } else {
            throw new RuntimeException("No OTP found for this email: " + regenerate.getEmail());
        }
    }

    }


