package com.NaukriChowk.Job_Wala.utils;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class OtpUtil {

    public String generateOtp() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String num = Integer.toString(randomNumber);
        while(num.length() < 6){
            num = "0"+num;
        }
        return  num;
    }
}
