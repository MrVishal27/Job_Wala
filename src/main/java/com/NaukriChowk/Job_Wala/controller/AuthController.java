package com.NaukriChowk.Job_Wala.controller;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import com.NaukriChowk.Job_Wala.response.LoginResponse;
import com.NaukriChowk.Job_Wala.response.RegisterResponse;
import com.NaukriChowk.Job_Wala.security.JwtProvider;
import com.NaukriChowk.Job_Wala.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody SignUpForm signUpForm) throws Exception {

        RegisterResponse response = authService.registerEmployee(signUpForm);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        LoginResponse loginResponse = authService.loginEmployee(loginForm);
        return ResponseEntity.ok(loginResponse);
    }

}
