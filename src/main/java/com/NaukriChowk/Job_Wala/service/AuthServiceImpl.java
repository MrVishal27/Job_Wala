package com.NaukriChowk.Job_Wala.service;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import com.NaukriChowk.Job_Wala.model.*;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import com.NaukriChowk.Job_Wala.response.LoginResponse;
import com.NaukriChowk.Job_Wala.response.RegisterResponse;
import com.NaukriChowk.Job_Wala.security.JwtProvider;
import com.NaukriChowk.Job_Wala.utils.EmailUtil;
import com.NaukriChowk.Job_Wala.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenServiceImpl refreshTokenService;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;
    private final  OtpService otpService;


    @Override
    public ResponseEntity<?> registerUser(SignUpForm signUpForm)  {
        RegisterResponse response= new RegisterResponse();

        if (userRepository.existsByEmail(signUpForm.getEmail())) {
            return ResponseEntity.badRequest().body("Fail -> Email is already in use!");
        }

        // Creating user's account
        User user = new User();
        user.setFirstName(signUpForm.getFirstName());
        user.setLastName(signUpForm.getLastName());
        user.setEmail(signUpForm.getEmail());
        user.setAge(signUpForm.getAge());
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        user.setOtpGeneratedTime(LocalDateTime.now());


        if (signUpForm != null && signUpForm.getPassword() != null) {
            String generatedOtp = otpUtil.generateOtp();

            otpService.saveOtp(signUpForm.getEmail(), generatedOtp);

            // Check if email sending is successful
            if (emailUtil.sendOtpEmail(signUpForm.getEmail(), generatedOtp)) {
                 userRepository.save(user);
                response.setMessage("User Registered Successfully. Check your email for OTP verification.");
                response.setStatusCode(200);
            } else {
                response.setStatusCode(500);
                response.setMessage("Failed to send OTP email. Please try again.");
            }
        }
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<?> loginUser(LoginForm loginForm) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(),
                        loginForm.getPassword())
                );

        User user = userRepository.findByEmail(loginForm.getEmail())
                .orElseThrow(() -> new RuntimeException("Fail! -> User not found."));

        if (!user.isVerified()) {
            return ResponseEntity.badRequest().body( "User is not verified. please verify then login");
        }

        var jwtToken = jwtProvider.generateToken(user);

        String ourEmail = jwtProvider.getUsernameFromToken(jwtToken);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(ourEmail);
//        refreshToken = refreshTokenService.save(refreshToken);

        return ResponseEntity.ok(new LoginResponse("success", jwtToken, refreshToken.getToken()));

    }

}
