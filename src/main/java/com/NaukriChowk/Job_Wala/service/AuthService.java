package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {

     ResponseEntity<?> registerUser(SignUpForm signUpForm) ;

     ResponseEntity<?> loginUser(LoginForm loginForm);


}
