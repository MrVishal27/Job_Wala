package com.NaukriChowk.Job_Wala.dto.authrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationRequest {
    private String email;
    private String otp;
}
