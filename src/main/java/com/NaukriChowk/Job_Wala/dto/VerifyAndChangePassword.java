package com.NaukriChowk.Job_Wala.dto;

import lombok.Data;

@Data
public class VerifyAndChangePassword {
    private  String email;
    private String otp;
    private String newPassword;
}
