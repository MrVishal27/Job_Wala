package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import com.NaukriChowk.Job_Wala.dto.TokenRefreshRequest;
import com.NaukriChowk.Job_Wala.response.LoginResponse;
import com.NaukriChowk.Job_Wala.response.RefreshTokenResponse;
import com.NaukriChowk.Job_Wala.response.RegisterResponse;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {

     RegisterResponse registerEmployee(SignUpForm signUpForm) throws Exception;
     LoginResponse loginEmployee(LoginForm loginForm);
     RefreshTokenResponse refresh(TokenRefreshRequest tokenRefreshRequest);

}
