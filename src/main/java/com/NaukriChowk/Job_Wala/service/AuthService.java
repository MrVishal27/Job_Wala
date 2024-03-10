package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.authrequest.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {

     ResponseEntity<?> registerUser(SignUpForm signUpForm) ;

     ResponseEntity<?> loginUser(LoginForm loginForm);

     ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest);
     ResponseEntity<?> forgetPassword(ForgetPasswordRequest forgetPasswordRequest);

     ResponseEntity<?> verifyAndChangePassword(VerifyAndChangePassword verifyAndChangePassword);

}
