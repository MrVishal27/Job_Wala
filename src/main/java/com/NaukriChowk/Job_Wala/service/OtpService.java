package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.model.OtpEntity;
import com.NaukriChowk.Job_Wala.repo.OtpRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;

    public void saveOtp(String email, String otp) {

            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otp);
            otpRepository.save(otpEntity);

    }
    public boolean verifyOtp(String email, String enteredOtp) {
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);
        return optionalOtpEntity.map(otpEntity -> otpEntity.getOtp().equals(enteredOtp)).orElse(false);
    }
}

