package com.NaukriChowk.Job_Wala.controller;

import com.NaukriChowk.Job_Wala.dto.LoginForm;
import com.NaukriChowk.Job_Wala.dto.OtpVerificationRequest;
import com.NaukriChowk.Job_Wala.dto.SignUpForm;
import com.NaukriChowk.Job_Wala.dto.TokenRefreshRequest;
import com.NaukriChowk.Job_Wala.model.RefreshToken;
import com.NaukriChowk.Job_Wala.model.User;
import com.NaukriChowk.Job_Wala.repo.UserRepository;
import com.NaukriChowk.Job_Wala.response.RefreshTokenResponse;
import com.NaukriChowk.Job_Wala.security.JwtProvider;
import com.NaukriChowk.Job_Wala.service.AuthService;
import com.NaukriChowk.Job_Wala.service.OtpService;
import com.NaukriChowk.Job_Wala.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpForm signUpForm)  {

        return authService.registerUser(signUpForm);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginForm) {
        return authService.loginUser(loginForm);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshJwtToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(tokenRefreshRequest.getRefreshToken());
        User user = refreshToken.getUser();

        String accessToken = jwtProvider.generateToken((UserDetails) user);

        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpVerificationRequest request) {
        String email = request.getEmail();
        String enteredOtp = request.getOtp();

        if (otpService.verifyOtp(email, enteredOtp)) {
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setVerified(true);
            userRepository.save(user);
            return "OTP is valid. User is verified!";
        } else {
            return "Invalid OTP. User verification failed.";
        }
    }


}
