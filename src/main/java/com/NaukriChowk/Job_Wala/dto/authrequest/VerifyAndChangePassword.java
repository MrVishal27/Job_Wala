package com.NaukriChowk.Job_Wala.dto.authrequest;

import lombok.Data;

@Data
public class VerifyAndChangePassword {
    private  String email;
    private String otp;
    private String newPassword;
}
