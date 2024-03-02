package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.model.RefreshToken;
import org.springframework.stereotype.Component;

@Component
public interface RefreshTokenService {

     RefreshToken createRefreshToken(String email) ;
     RefreshToken verifyRefreshToken(String refreshToken);

}
